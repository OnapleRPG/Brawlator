package com.onaple.brawlator.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.table.LootTable;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class LootTableSerializer implements TypeSerializer<LootTable> {
    @Override
    public LootTable deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String tableName = value.getString();
        return Brawlator.getConfigurationHandler().getLootTableList().stream()
                .filter(lootTable -> lootTable.getName().equals(tableName))
                .findFirst()
                .orElseThrow(() -> new ObjectMappingException("Table with name " + tableName + " not found"));
    }

    @Override
    public void serialize(TypeToken<?> type, LootTable obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
