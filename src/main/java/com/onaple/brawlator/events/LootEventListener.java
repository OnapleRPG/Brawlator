package com.onaple.brawlator.events;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.beans.loot.Loot;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.HarvestEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;

public class LootEventListener {

    @Listener
    public void onLoot(DropItemEvent.Destruct event, @First Entity entity, @Root EntityDamageSource source){
        Optional<Loot> arg2 = entity.get(BrawlatorKeys.LOOT);
        Brawlator.getLogger().info("entity {}, has data {}",entity, arg2);
        if(arg2.isPresent()){
            ItemStack fetch = arg2.get().fetch();
            Location<World> location = entity.getLocation();
            Extent extent = location.getExtent();
            Entity itemEntity = extent.createEntity(EntityTypes.ITEM, location.getPosition());
            itemEntity.offer(Keys.REPRESENTED_ITEM, fetch.createSnapshot());
            event.getEntities().add(itemEntity);
        }
    }
}
