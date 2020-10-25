package com.onaple.brawlator.events;

import com.onaple.brawlator.Brawlator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodCall;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.impl.AbstractEvent;

import javax.inject.Singleton;

@Singleton
public class DynamicEntityEventGenerator {


    public static class BrawlatorEvent extends AbstractEvent{
        protected Entity entity;
        protected Player player;
       protected Cause cause;

        public BrawlatorEvent(Entity entity, Player player, Cause cause) {
            this.entity = entity;
            this.player = player;
            this.cause = cause;
        }

        public Entity getEntity() {
            return entity;
        }

        public Player getPlayer() {
            return player;
        }

        @Override
        public Cause getCause() {
            return cause;
        }

        @Override
        public String toString() {
            return "BrawlatorEvent{" +
                    "entity=" + entity +
                    ", player=" + player +
                    ", cause=" + cause +
                    '}';
        }
    }

    public Class<?> generate(String entityName, Class<?> event) {
        String s = entityName.replaceAll("\\W", "");
        try {
            return new ByteBuddy()
                    .subclass(BrawlatorEvent.class, ConstructorStrategy.Default.NO_CONSTRUCTORS)
                    .name( s + event.getSimpleName())
                    .defineConstructor(Visibility.PUBLIC)
                    .withParameters(Entity.class, Player.class, Cause.class)
                    .intercept(MethodCall.invoke(BrawlatorEvent.class.getConstructor(Entity.class, Player.class,Cause.class))
                            .withArgument(0, 1, 2)
                    )
                    .make()
                    .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                    .getLoaded();
        } catch (NoSuchMethodException e) {
            Brawlator.getLogger().error("No constructor", e);
        }
        return null;
    }

}
