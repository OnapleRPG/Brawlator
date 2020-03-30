package com.onaple.brawlator.data.beans.table;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.probability.Probable;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ConfigSerializable
public class LootTable implements Probable {
    public LootTable() {
        this.inherits = new ArrayList<>();
        this.loot = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Setting
    private String name;
    @Setting
    private double probability;

    @Setting
    private List<LootTable> inherits;

    @Setting
    private List<Loot> loot;

    public List<LootTable> getInherits() {
        return inherits;
    }

    private List<Loot> getLoot() {
        return  loot;
    }


    @Override
    public double getProbability() {
        return probability;
    }

    public List<Loot> fetchLoots(double probability){
        List<Loot> allLoot = new ArrayList<>();
        allLoot.addAll(getLoot());
        allLoot.addAll(inherits.stream().map(LootTable::getLoot)
                .flatMap(loots -> loots.stream()).collect(Collectors.toList()));
        return allLoot.stream()
                .filter(loot1 -> loot1.getWeight() > probability)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "LootTable{" +
                "name='" + name + '\'' +
                ", probability=" + probability +
                ", inherits=" + inherits +
                ", loot=" + loot +
                '}';
    }
}
