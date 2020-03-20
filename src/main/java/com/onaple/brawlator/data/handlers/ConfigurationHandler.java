package com.onaple.brawlator.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.LootTable;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterListBean;
import com.onaple.brawlator.data.beans.SpawnerTypeBean;
import com.onaple.brawlator.data.beans.SpawnerTypeListBean;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.data.serializers.LootSerializer;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConfigurationHandler {

    public ConfigurationHandler() {
        lootTableList = new ArrayList<>();
        monsterList = new ArrayList<>();
        spawnerTypeList = new ArrayList<>();
    }




    private List<LootTable> lootTableList;
    private List<MonsterBean> monsterList;
    private List<SpawnerTypeBean> spawnerTypeList;


    public List<LootTable> getLootTableList() {
        return lootTableList;
    }
    public List<MonsterBean> getMonsterList(){
        return monsterList;
    }
    public List<SpawnerTypeBean> getSpawnerTypeList(){
        return spawnerTypeList;
    }

    /**
     * Read monsters configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public int readMonstersConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        monsterList = configurationNode.getValue(MonsterListBean.TYPE).getMonsters();
        return monsterList.size();
    }

    /**
     * Read spawners types configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public int readSpawnerTypesConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        spawnerTypeList = configurationNode.getValue(SpawnerTypeListBean.TYPE).getSpawners();
        return spawnerTypeList.size();
    }

    public int readLootTableConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        lootTableList = configurationNode.getNode("loots").getList(TypeToken.of(LootTable.class));
        return lootTableList.size();
    }

    /**
     * Load configuration from file
     * @param configName Name of the configuration in the configuration folder
     * @return Configuration ready to be used
     */
    public CommentedConfigurationNode loadConfiguration(String configName , ConfigurationOptions  options) throws IOException {
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(configName)).build();
        return configLoader.load(options);
    }
}
