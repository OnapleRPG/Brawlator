package com.onaple.brawlator.data.beans.loot;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public interface Loot extends DataSerializable {
    ItemStack fetch();
    static Loot empty(){
        return new Loot() {
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
                return null;
            }
        };
    }
}
