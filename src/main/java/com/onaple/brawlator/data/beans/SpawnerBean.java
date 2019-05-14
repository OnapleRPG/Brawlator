package com.onaple.brawlator.data.beans;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.World;

import java.util.Date;
import java.util.Optional;

public class SpawnerBean {
    private int id;
    private Vector3i position;
    private String worldName;
    private String spawnerTypeName;
    private String monsterName;
    private World world = null;
    private SpawnerTypeBean spawnerType = null;
    private Date lastSpawn = new Date();

    public SpawnerBean(int id, Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
        this.id = id;
        this.position = position;
        this.worldName = worldName;
        this.spawnerTypeName = spawnerTypeName;
        this.monsterName = monsterName;
    }

    public SpawnerBean(Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
        this.position = position;
        this.worldName = worldName;
        this.spawnerTypeName = spawnerTypeName;
        this.monsterName = monsterName;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Vector3i getPosition() {
        return position;
    }
    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public String getWorldName() {
        return worldName;
    }
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public String getSpawnerTypeName() {
        return spawnerTypeName;
    }
    public void setSpawnerTypeName(String spawnerTypeName) {
        this.spawnerTypeName = spawnerTypeName;
    }

    public String getMonsterName() {
        return monsterName;
    }
    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    public SpawnerTypeBean getSpawnerType() {
        return spawnerType;
    }
    public void setSpawnerType(SpawnerTypeBean spawnerType) {
        this.spawnerType = spawnerType;
    }

    public Date getLastSpawn() {
        return lastSpawn;
    }
    public void setLastSpawn(Date lastSpawn) {
        this.lastSpawn = lastSpawn;
    }
}
