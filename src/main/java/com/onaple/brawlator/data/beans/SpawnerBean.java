package com.onaple.brawlator.data.beans;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.World;

import java.util.Date;

public class SpawnerBean {
    /**
     * Spawner ID
     */
    private int id;

    /**
     * Spawner position
     */
    private Vector3i position;

    /**
     * Spawner world name
     */
    private String worldName;

    /**
     * Spawner type name, stored in configuration
     */
    private String spawnerTypeName;

    /**
     * Monster name, stored in configuration or vanilla one
     */
    private MonsterBean monster;

    /**
     * World reference set afterwards
     */
    private World world = null;

    /**
     * Spawner type set afterwards
     */
    private SpawnerTypeBean spawnerType = null;

    /**
     * Last spawn date
     */
    private Date lastSpawn = new Date();

    public SpawnerBean(int id, Vector3i position, String worldName, String spawnerTypeName, MonsterBean monster) {
        this.id = id;
        this.position = position;
        this.worldName = worldName;
        this.spawnerTypeName = spawnerTypeName;
        this.monster = monster;
    }

    public SpawnerBean(Vector3i position, String worldName, String spawnerTypeName, MonsterBean monster) {
        this.position = position;
        this.worldName = worldName;
        this.spawnerTypeName = spawnerTypeName;
        this.monster = monster;
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

    public MonsterBean getMonster() {
        return monster;
    }

    public void setMonster(MonsterBean monster) {
        this.monster = monster;
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
