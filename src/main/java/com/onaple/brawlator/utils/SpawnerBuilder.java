package com.onaple.brawlator.utils;

import com.flowpowered.math.vector.Vector3i;
import com.onaple.brawlator.data.beans.SpawnerBean;
import com.onaple.brawlator.data.beans.SpawnerTypeBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import java.util.Optional;

public class SpawnerBuilder {
    public static SpawnerBean buildSpawner(int id, Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
        SpawnerBean spawner = new SpawnerBean(id, position, worldName, spawnerTypeName, monsterName);

        spawner.setSpawnerType(getSpawnerType(spawnerTypeName).orElse(null));
        spawner.setWorld(getSpawnerWorld(worldName).orElse(null));

        return spawner;
    }
    public static SpawnerBean buildSpawner(Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
        SpawnerBean spawner = new SpawnerBean(position, worldName, spawnerTypeName, monsterName);

        spawner.setSpawnerType(getSpawnerType(spawnerTypeName).orElse(null));
        spawner.setWorld(getSpawnerWorld(worldName).orElse(null));

        return spawner;
    }

    private static Optional<SpawnerTypeBean> getSpawnerType(String typeName) {
        return ConfigurationHandler.getSpawnerTypeList().stream().filter(m -> m.getName().toLowerCase().equals(typeName.toLowerCase())).findAny();
    }

    private static Optional<World> getSpawnerWorld(String worldName) {
        return Sponge.getServer().getWorld(worldName);
    }
}
