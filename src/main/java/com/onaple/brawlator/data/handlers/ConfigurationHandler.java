package com.onaple.brawlator.data.handlers;

import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterListBean;
import com.onaple.brawlator.data.beans.SpawnerTypeBean;
import com.onaple.brawlator.data.beans.SpawnerTypeListBean;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler {
    private static List<MonsterBean> monsterList = new ArrayList<>();
    public static List<MonsterBean> getMonsterList(){
        return monsterList;
    }

    private static List<SpawnerTypeBean> spawnerTypeList = new ArrayList<>();
    public static List<SpawnerTypeBean> getSpawnerTypeList(){
        return spawnerTypeList;
    }

    /**
     * Read monsters configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readMonstersConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        monsterList = configurationNode.getValue(MonsterListBean.TYPE).getMonsters();
        return monsterList.size();
    }

    /**
     * Read spawners types configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readSpawnerTypesConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        spawnerTypeList = configurationNode.getValue(SpawnerTypeListBean.TYPE).getSpawners();
        return spawnerTypeList.size();
    }

    /**
     * Load configuration from file
     * @param configName Name of the configuration in the configuration folder
     * @return Configuration ready to be used
     */
    public static CommentedConfigurationNode loadConfiguration(String configName) throws IOException {
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(configName)).build();
        CommentedConfigurationNode configNode;
        return configLoader.load();
    }
}
