package com.onaple.brawlator;

import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.events.DynamicEntityEventGenerator;
import com.onaple.brawlator.events.EventConsumer;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Argument;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.TargetEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

@Singleton
public class ScriptManager {

    private ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    @Inject
    private ConfigurationHandler configurationHandler;
    @Inject
    private DynamicEntityEventGenerator dynamicEntityEventGenerator;
    @Inject
    private MonsterAction monsterAction;

    public Object load(MonsterBean monsterBean) throws IOException, ScriptException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        AnnotationDescription annotation = AnnotationDescription.Builder.ofType(Listener.class)
                .build();

        String scriptObject = "{\"name\":\"lib.js\",\"script\":\"var listen = function (func){ return  new com.onaple.brawlator.events.EventConsumer(func)}\"}";
        engine.eval("load(" + scriptObject + ")");


        configurationHandler.getMonsterScript(monsterBean).forEach(s ->
        {
            try {
                engine.eval(s);
            } catch (ScriptException e) {
                Brawlator.getLogger().error("Error while reading script {}.",s, e);
            }
        });

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
        generatedDynamicListener.make().saveIn(new File("/home/hugo/Documents/"));
        return generatedDynamicListener.make().load(getClass().getClassLoader()).getLoaded().getDeclaredConstructor().newInstance();
    }


    public Object AddListener() throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        DynamicType.Builder<Object> classBuilder = new ByteBuddy()
                .subclass(Object.class)
                .name("com.onaple.brawlator.MonsterLifeCycleEventListener");
        for(Map.Entry<String,Class<?>> entry : Brawlator.getGlobalConfig().getEvents().entrySet()) {
            classBuilder = classBuilder
                    .defineMethod(entry.getKey(), void.class, Visibility.PUBLIC)
                    .withParameter(entry.getValue(), "event")
                 /*   .withParameter(Entity.class, "entity").annotateParameter(AnnotationDescription.Builder.ofType(First.class)
                    .build())
                    .withParameter(Player.class, "player").annotateParameter(AnnotationDescription.Builder.ofType(Root.class)
                    .build())*/
                    .intercept(MethodDelegation.to(new MethodListener(monsterAction, entry.getKey())))
                    .annotateMethod(AnnotationDescription.Builder.ofType(Listener.class).build());
        }
        classBuilder.make().saveIn(new File("/home/hugo/Documents/"));
         return classBuilder.make().load(getClass().getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();

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

    public static class MethodListener{

        private  MonsterAction monsterAction;

        private String eventName;

        public MethodListener(MonsterAction monsterAction, String eventName) {
            this.monsterAction = monsterAction;
            this.eventName = eventName;
        }

        public void intercept(@Argument(0) TargetEntityEvent event/*, @Argument(1) Entity entity, @Argument(2) Player player*/) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            Optional<String> name = event.getTargetEntity().get(Keys.DISPLAY_NAME).map(Text::toPlain);
            if(name.isPresent()){
                try {
                    MonsterBean monster = monsterAction.getMonster(name.get());
                    Optional<Player> first = event.getCause().first(Player.class);
                    if(first.isPresent()) {
                        Event brawlatorEvent = (Event) monster.getEvents().get(eventName).getConstructor(Entity.class, Player.class, Cause.class).newInstance(event.getTargetEntity(), first.get(), event.getCause());
                        Brawlator.getLogger().info("emit event {}", brawlatorEvent);
                        Sponge.getEventManager().post(brawlatorEvent);
                    }
                } catch (MonsterNotFoundException e){
                    Brawlator.getLogger().info("Can't post event for monster named {}.",name.get(),e);
                }
            }
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
