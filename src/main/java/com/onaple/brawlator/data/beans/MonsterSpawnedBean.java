package com.onaple.brawlator.data.beans;

import java.util.UUID;

public class MonsterSpawnedBean {
    /**
     * Monster spawned id
     */
    private int id;

    /**
     * Spawner ID from which the monster comes from
     */
    private int spawnerId;

    /**
     * Monster UUID
     */
    private UUID uuid;

    /**
     * Monster world name
     */
    private String worldName;

    public MonsterSpawnedBean(int spawnerId, UUID uuid, String worldName) {
        this.spawnerId = spawnerId;
        this.uuid = uuid;
        this.worldName = worldName;
    }

    public MonsterSpawnedBean(int id, int spawnerId, UUID uuid, String worldName) {
        this.id = id;
        this.spawnerId = spawnerId;
        this.uuid = uuid;
        this.worldName = worldName;
    }

    public int getSpawnerId() {
        return spawnerId;
    }
    public void setSpawnerId(int spawnerId) {
        this.spawnerId = spawnerId;
    }

    public UUID getUuid() {
        return uuid;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getWorldName() {
        return worldName;
    }
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }
}
