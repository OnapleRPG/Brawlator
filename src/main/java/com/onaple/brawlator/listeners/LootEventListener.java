package com.onaple.brawlator.listeners;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.events.BrawlatorEntityDiedEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.List;
import java.util.Optional;

public class LootEventListener {

    @Listener
    public void onLoot(DropItemEvent.Destruct event, @First Entity entity, @Root EntityDamageSource source) {
        Optional<List<Loot>> loots = entity.get(BrawlatorKeys.LOOT);

        if (source.getSource() instanceof Player) {
            BrawlatorEntityDiedEvent brawlatorEntityDiedEvent = new BrawlatorEntityDiedEvent(event.getCause(), entity, (Player) source.getSource());
            Sponge.getEventManager().post(brawlatorEntityDiedEvent);
            if (!brawlatorEntityDiedEvent.isCancelled()) {
                Brawlator.getLogger().info("entity {}, has data [{}]", entity, loots);
                if (loots.isPresent()) {
                    Location<World> location = entity.getLocation();
                    Extent extent = location.getExtent();
                    loots.get().forEach(loot -> {
                        ItemStack fetch = loot.fetch();
                        Entity itemEntity = extent.createEntity(EntityTypes.ITEM, location.getPosition());
                        itemEntity.offer(Keys.REPRESENTED_ITEM, fetch.createSnapshot());
                        event.getEntities().add(itemEntity);
                    });
                }
            }
        }
    }
}
