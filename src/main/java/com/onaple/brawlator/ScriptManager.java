package com.onaple.brawlator;

import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.events.DynamicEntityEventGenerator;
import com.onaple.brawlator.events.EventConsumer;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.TargetEntityEvent;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

@Singleton
public class ScriptManager {

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Inject
    private PluginContainer pluginContainer;
    @Inject
    private DynamicEntityEventGenerator dynamicEntityEventGenerator;

    public Object load(MonsterBean monsterBean) throws IOException, ScriptException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        AnnotationDescription annotation = AnnotationDescription.Builder.ofType(Listener.class)
                .build();

        String scriptObject = "{\"name\":\"lib.js\",\"script\":\"var listen = function (func){ return  new com.onaple.brawlator.events.EventConsumer(func)}\"}";
        engine.eval("load(" + scriptObject + ")");

        Asset scprit1 = pluginContainer.getAsset("myfile.js").get();

        engine.eval(scprit1.readString());

        String monsterClassName = monsterBean.getName().replaceAll("\\W", "");
        DynamicType.Builder<Object> generatedDynamicListener = new ByteBuddy()
                .subclass(Object.class)
                .name("$" + monsterClassName + "Listener");

        Brawlator.getLogger().info("loaded events : {}", Brawlator.getGlobalConfig().getEvents());

        for(Map.Entry<String, Class<?>> entry : Brawlator.getGlobalConfig().getEvents().entrySet()) {
            Optional<EventConsumer> consumer = getConsumer(entry.getKey());
            if (consumer.isPresent()) {
                String methodName = entry.getKey();
                Class<?> eventClass = entry.getValue();
                Class<?> generatedEvent = dynamicEntityEventGenerator.generate(monsterBean.getName(), eventClass);
                Brawlator.getLogger().info("generate event listener for {}", generatedEvent);
                monsterBean.getEvents().put(methodName, generatedEvent);
                generatedDynamicListener = generatedDynamicListener
                        .defineMethod(methodName, void.class, Visibility.PUBLIC)
                        .withParameter(generatedEvent)
                        .intercept(MethodDelegation.to(new EventHandlerInterceptor(consumer.get())))
                        .annotateMethod(annotation);
            }
        }
       // generatedDynamicListener.make().saveIn(new File("/home/hugo/Documents/"));
        return generatedDynamicListener.make().load(getClass().getClassLoader()).getLoaded().getDeclaredConstructor().newInstance();
    }

    public static class EventHandlerInterceptor {
        private final EventConsumer consumer;

        public EventHandlerInterceptor(EventConsumer consumer) {
            this.consumer = consumer;
        }

        public void intercept(@Argument(0) Object entity) {
            DynamicEntityEventGenerator.BrawlatorEvent brawlatorEvent = (DynamicEntityEventGenerator.BrawlatorEvent) entity;
            this.consumer.consume(brawlatorEvent.getEntity(), brawlatorEvent.getPlayer(),brawlatorEvent.getCause());
        }
    }


    private Optional<EventConsumer> getConsumer(String event){
        try {
          return Optional.of((EventConsumer) engine.get(event));
        } catch (IllegalArgumentException e) {
            Brawlator.getLogger().debug("no event named {}",event, e);
        } catch (NullPointerException e){
            Brawlator.getLogger().debug("Bad name {}",event, e);
        }
        return Optional.empty();
    }
}
