package com.onaple.brawlator.events;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.impl.AbstractEvent;

public class BrawlatorEntityDiedEvent extends AbstractEvent implements Cancellable {

    private final Cause cause;
    private final Entity entity;
    private final Player source;

    public BrawlatorEntityDiedEvent(Cause cause, Entity entity, Player source) {
        this.cause = cause;
        this.entity = entity;
        this.source = source;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public Player getSource() {
        return source;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return null;
    }
}
