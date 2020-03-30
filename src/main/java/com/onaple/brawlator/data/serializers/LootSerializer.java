package com.onaple.brawlator.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.data.beans.loot.ItemTypeLoot;
import com.onaple.brawlator.data.beans.loot.ItemizerPoolLoot;
import com.onaple.brawlator.data.beans.loot.ItemizerRefLoot;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.data.beans.loot.StackLoot;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class LootSerializer implements TypeSerializer<Loot> {
    @Override
    public Loot deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        lootConfig lootConfig = value.getValue(TypeToken.of(lootConfig.class));

        return lootConfig.toLoot();
    }

    @Override
    public void serialize(TypeToken<?> type, Loot obj, ConfigurationNode value) throws ObjectMappingException {
    // Maybe I will Implement this one day
    }

    @ConfigSerializable
    private static class lootConfig{

        @Setting
        private ItemType type;

        @Setting
        private ItemStack stack;

        @Setting
        private String poolId;

        @Setting
        private String ref;
        @Setting
        private double weight;

        public Loot toLoot() throws ObjectMappingException {
            if(type != null) {
                return new ItemTypeLoot(weight,type);
            }
            if(stack != null){
                return new StackLoot(weight,stack);
            }
            if(poolId != null){
                return new ItemizerPoolLoot(weight,poolId);
            }
            if(ref != null){
                return new ItemizerRefLoot(weight,ref);
            }
            throw new ObjectMappingException("No Item reference found");
        }

    }
}
