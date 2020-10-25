package com.onaple.brawlator.data.beans;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@ConfigSerializable
public class GlobalConfig {
    @Setting
    private boolean enableNaturalSpawning;
    @Setting
    private Map<String,Class<?>> events;

    public boolean isEnableNaturalSpawning() {
        return enableNaturalSpawning;
    }

    public void setEnableNaturalSpawning(boolean enableNaturalSpawning) {
        this.enableNaturalSpawning = enableNaturalSpawning;
    }

    public Map<String, Class<?>> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Class<?>> events) {
        this.events = events;
    }

    public GlobalConfig(){}

    public GlobalConfig(boolean enableNaturalSpawning, Map<String, Class<?>> events) {
        this.enableNaturalSpawning = enableNaturalSpawning;
        this.events = events;
    }

    public static GlobalConfig defaultConfig(){
        return new GlobalConfig(false, new HashMap<>());
    }

    @Override
    public String toString() {
        return "GlobalConfig{" +
                "enableNaturalSpawning=" + enableNaturalSpawning +
                '}';
    }
}
