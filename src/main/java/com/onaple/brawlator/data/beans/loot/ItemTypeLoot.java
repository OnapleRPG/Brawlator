package com.onaple.brawlator.data.beans.loot;

import com.onaple.brawlator.Brawlator;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemTypeLoot implements Loot {

    ItemType type;

    public ItemTypeLoot(ItemType type) {
        this.type = type;
    }

    @Override
    public ItemStack fetch() {
        Brawlator.getLogger().info("Item Type = {}", type);
        return ItemStack.builder().itemType(type).build();
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(DataQuery.of("type"), this.type)
                .set(Queries.CONTENT_VERSION, getContentVersion());
    }
}
