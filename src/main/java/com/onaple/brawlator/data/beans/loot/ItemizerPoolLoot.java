package com.onaple.brawlator.data.beans.loot;

import com.onaple.brawlator.Brawlator;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.inventory.ItemStack;

public class ItemizerPoolLoot implements Loot {

    private String poolId;

    public ItemizerPoolLoot(String pooId) {
        this.poolId = pooId;
    }

    @Override
    public ItemStack fetch() {
        Brawlator.getLogger().info("poolsId {}", poolId);
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(DataQuery.of("PoolId"), this.poolId)
                .set(Queries.CONTENT_VERSION, getContentVersion());
    }
}
