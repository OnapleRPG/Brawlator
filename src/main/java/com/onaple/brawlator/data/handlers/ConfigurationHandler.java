package com.onaple.brawlator.data.handlers;

import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterListBean;
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

    /**
     * Read spawners configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readMonstersConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        monsterList = configurationNode.getValue(MonsterListBean.TYPE).getMonsters();
        return monsterList.size();
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
