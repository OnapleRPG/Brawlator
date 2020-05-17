package com.onaple.brawlator;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.data.beans.loot.Loot;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class BrawlatorKeys {
    public static final Key<ListValue<Loot>> LOOT;
    public static final Key<Value<Integer>> EXPERIENCE ;
static {
    LOOT = Key.builder()
            .id("monster.loot")
            .name("Monster loot")
            .type(new TypeToken<ListValue<Loot>>() {
            })
            .query(DataQuery.of(".", "loot"))
            .build();
    EXPERIENCE = Key.builder()
            .id("monster.xp.amount")
            .name("monster experience")
            .type(TypeTokens.INTEGER_VALUE_TOKEN)
            .query(DataQuery.of(".","xp","amount"))
            .build();
    }

}
