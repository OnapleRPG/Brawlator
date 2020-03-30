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

public class ItemizerRefLoot extends PonderedLoot {

    private String ref;

    public ItemizerRefLoot(double weigth, String ref) {
        super(weigth);
        this.ref = ref;
    }

    @Override
    public ItemStack fetch() {
        Optional<IItemService> optionalIItemService = Sponge.getServiceManager().provide(IItemService.class);
        if (optionalIItemService.isPresent()) {
            IItemService iItemService = optionalIItemService.get();
            try {
                return iItemService.retrieve(ref).orElseThrow(() -> new ItemNotPresentException(ref));
            } catch (ItemNotPresentException e) {
                Brawlator.getLogger().error(e.getMessage());
            }
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
                    .set(DataQuery.of("ref"), this.ref)
                    .set(Queries.CONTENT_VERSION, getContentVersion());
    }

    @Override
    public String toString() {
        return "ItemizerRefLoot{" +
                "ref='" + ref + '\'' +
                ", weigth=" + weigth +
                '}';
    }
}
