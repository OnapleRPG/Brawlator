package com.onaple.brawlator.events;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;

@FunctionalInterface
public interface EventConsumer {
    void consume(Entity entity, Player player, Cause cause);
}
