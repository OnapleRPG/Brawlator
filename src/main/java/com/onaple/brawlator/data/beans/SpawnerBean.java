package com.onaple.brawlator.data.beans;

import com.flowpowered.math.vector.Vector3i;

public class SpawnerBean {
    private int id;
    private Vector3i position;
    private int spawnerConfigId;

    public SpawnerBean(int id, Vector3i position, int spawnerConfigId) {
        this.id = id;
        this.position = position;
        this.spawnerConfigId = spawnerConfigId;
    }

    public SpawnerBean(Vector3i position, int spawnerConfigId) {
        this.position = position;
        this.spawnerConfigId = spawnerConfigId;
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

    public int getSpawnerConfigId() {
        return spawnerConfigId;
    }
    public void setSpawnerConfigId(int spawnerConfigId) {
        this.spawnerConfigId = spawnerConfigId;
    }
}
