package com.onaple.brawlator.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.world.biome.BiomeType;

@ConfigSerializable
public class NaturalSpawnData {
    @Setting
    private float probability;
    @Setting
    private BiomeType biomeType;
    @Setting
    private int maxHeight;

    public NaturalSpawnData() {
    }

    public float getProbability() {
        return probability;
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public String toString() {
        return "NaturalSpawnData{" +
                "probability=" + probability +
                ", biomeType=" + biomeType +
                ", maxHeight=" + maxHeight +
                '}';
    }
}
