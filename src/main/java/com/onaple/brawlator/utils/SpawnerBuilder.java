package com.onaple.brawlator.utils;

import com.flowpowered.math.vector.Vector3i;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.SpawnerBean;
import com.onaple.brawlator.data.beans.SpawnerTypeBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class SpawnerBuilder {

    @Inject
    ConfigurationHandler configurationHandler;

    public SpawnerBean buildSpawner(int id, Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
       SpawnerBean spawner = new SpawnerBean(id, position, worldName, spawnerTypeName, getMonster(monsterName));

        spawner.setSpawnerType(getSpawnerType(spawnerTypeName).orElse(null));
        spawner.setWorld(getSpawnerWorld(worldName).orElse(null));

        return spawner;
    }
    public SpawnerBean buildSpawner(Vector3i position, String worldName, String spawnerTypeName, String monsterName) {
        SpawnerBean spawner = new SpawnerBean(position, worldName, spawnerTypeName, getMonster(monsterName));

        spawner.setSpawnerType(getSpawnerType(spawnerTypeName).orElse(null));
        spawner.setWorld(getSpawnerWorld(worldName).orElse(null));

        return spawner;
    }

    private Optional<SpawnerTypeBean> getSpawnerType(String typeName) {
        return configurationHandler.getSpawnerTypeList().stream().filter(m -> m.getName().toLowerCase().equals(typeName.toLowerCase())).findAny();
    }

    private Optional<World> getSpawnerWorld(String worldName) {
        return Sponge.getServer().getWorld(worldName);
    }

    private MonsterBean getMonster(String name){
        return configurationHandler.getMonsterList().stream().filter(monsterBean -> monsterBean.getName().equalsIgnoreCase(name)).findFirst().orElse(null);

    }
}
