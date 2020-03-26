package com.onaple.brawlator.data.beans.loot;

import com.onaple.brawlator.Brawlator;
import com.onaple.itemizer.exception.ItemNotPresentException;
import com.onaple.itemizer.service.IItemService;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.Queries;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class ItemizerPoolLoot implements Loot {

    private String poolId;

    public ItemizerPoolLoot(String pooId) {
        this.poolId = pooId;
    }

    @Override
    public ItemStack fetch() {
        Optional<IItemService> optionalIItemService = Sponge.getServiceManager().provide(IItemService.class);
        if (optionalIItemService.isPresent()) {
            IItemService iItemService = optionalIItemService.get();
                return iItemService.fetch(poolId).orElse(ItemStack.empty());

        } else {
            Brawlator.getLogger().error("Itemizer plugin not found");
        }
        return ItemStack.empty();
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
