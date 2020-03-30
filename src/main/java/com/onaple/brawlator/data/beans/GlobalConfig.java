package com.onaple.brawlator.data.beans;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GlobalConfig {
    @Setting
    private boolean enableNaturalSpawning;

    public boolean isEnableNaturalSpawning() {
        return enableNaturalSpawning;
    }

    public void setEnableNaturalSpawning(boolean enableNaturalSpawning) {
        this.enableNaturalSpawning = enableNaturalSpawning;
    }

    public GlobalConfig(){}

    private GlobalConfig(boolean enableNaturalSpawning) {
        this.enableNaturalSpawning = enableNaturalSpawning;
    }

    public static GlobalConfig defaultConfig(){
        return new GlobalConfig(false);
    }

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "enableNaturalSpawning=" + enableNaturalSpawning +
                '}';
    }
}
