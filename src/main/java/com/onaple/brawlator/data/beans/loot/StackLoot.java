package com.onaple.brawlator.data.beans.loot;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.inventory.ItemStack;


public class StackLoot implements Loot {

    private ItemStack stack;

    public StackLoot(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public ItemStack fetch() {
        return stack;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(DataQuery.of("stack"), this.stack)
                .set(Queries.CONTENT_VERSION, getContentVersion());
    }
}
