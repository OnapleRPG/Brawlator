package com.onaple.brawlator.data.beans;

import com.onaple.brawlator.data.beans.loot.Loot;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class LootTable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Loot getLoot() {
        return loot;
    }

    public void setLoot(Loot loot) {
        this.loot = loot;
    }

    @Setting
    private String name;

    @Setting
    private Loot loot;

    @Override
    public String toString() {
        return "LootTable{" +
                "name='" + name + '\'' +
                ", loot="+ loot.getClass() +" " + loot +
                '}';
    }
}
