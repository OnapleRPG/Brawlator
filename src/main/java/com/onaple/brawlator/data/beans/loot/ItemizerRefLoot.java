package com.onaple.brawlator.data.beans.loot;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemizerRefLoot implements Loot {

    private String ref;

    public ItemizerRefLoot(String ref) {
        this.ref = ref;
    }

    @Override
    public ItemStack fetch() {
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
            return DataContainer.createNew()
                    .set(DataQuery.of("ref"), this.ref)
                    .set(Queries.CONTENT_VERSION, getContentVersion());
    }
}
