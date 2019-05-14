package com.onaple.brawlator.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class SpawnerTypeBean {
    /**
     * Spawner type name, so it can be identified
     */
    @Setting(value="name")
    private String name;
    /**
     * Rate at which the mobs can spawn
     */
    @Setting(value="spawnRate")
    private double spawnRate;
    /**
     * Max range at which the mob can spawn
     */
    @Setting(value="maxSpawnRange")
    private double maxSpawnRange;
    /**
     * Max mob quantity the spawner can have around it
     */
    @Setting(value="quantityMax")
    private int quantityMax;

    public SpawnerTypeBean() {
    }

    public String getName() {
        return name;
    }
    public double getSpawnRate() {
        return spawnRate;
    }
    public double getMaxSpawnRange() {
        return maxSpawnRange;
    }
    public int getQuantityMax() {
        return quantityMax;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSpawnRate(double spawnRate) {
        this.spawnRate = spawnRate;
    }
    public void setMaxSpawnRange(double maxSpawnRange) {
        this.maxSpawnRange = maxSpawnRange;
    }
    public void setQuantityMax(int quantityMax) {
        this.quantityMax = quantityMax;
    }
}
