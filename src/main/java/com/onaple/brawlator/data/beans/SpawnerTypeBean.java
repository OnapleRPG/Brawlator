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
    @Setting(value="rate")
    private double rate;

    /**
     * Max range at which the mob can go without triggering despawn
     */
    @Setting(value="maxRoamRange")
    private double maxRoamRange;

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
    public double getRate() {
        return rate;
    }
    public double getMaxRoamRange() {
        return maxRoamRange;
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
    public void setRate(double rate) {
        this.rate = rate;
    }
    public void setMaxRoamRange(double maxRoamRange) {
        this.maxRoamRange = maxRoamRange;
    }
    public void setMaxSpawnRange(double maxSpawnRange) {
        this.maxSpawnRange = maxSpawnRange;
    }
    public void setQuantityMax(int quantityMax) {
        this.quantityMax = quantityMax;
    }
}
