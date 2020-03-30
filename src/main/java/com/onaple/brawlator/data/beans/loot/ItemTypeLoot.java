package com.onaple.brawlator.data.beans.loot;

import com.onaple.brawlator.Brawlator;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemTypeLoot extends PonderedLoot {

    ItemType type;

    public ItemTypeLoot(double weigth, ItemType type) {
        super(weigth);
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

    @Override
    public String toString() {
        return "ItemTypeLoot{" +
                "type=" + type +
                ", weigth=" + weigth +
                '}';
    }
}
