package com.onaple.brawlator;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.data.beans.loot.Loot;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.ListValue;

public class BrawlatorKeys {
    public static final Key<ListValue<Loot>> LOOT;
static {
    LOOT = Key.builder()
            .id("monster.loot")
            .name("Monster loot")
            .type(new TypeToken<ListValue<Loot>>() {
            })
            .query(DataQuery.of(".", "loot"))
            .build();
    }
}
