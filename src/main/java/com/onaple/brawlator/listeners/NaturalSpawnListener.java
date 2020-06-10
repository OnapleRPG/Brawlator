package com.onaple.brawlator.listeners;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.probability.ProbabilityFetcher;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;

import java.util.List;
import java.util.Optional;

public class NaturalSpawnListener {

    private final MonsterAction monsterAction;
    private final ProbabilityFetcher fetcher;

    public NaturalSpawnListener(MonsterAction monsterAction,ProbabilityFetcher fetcher) {
        this.monsterAction = monsterAction;
        this.fetcher = fetcher;
    }

    @Listener
    public void onNaturalSpawn(SpawnEntityEvent event){

        if(!event.getCause().root().getClass().getName().contains("net.minecraft.world.WorldServer")){
            return;
        }

       for(int i = 0; i< event.getEntities().size(); i++) {
           Entity entityToReplace = event.getEntities().get(i);
           EntityType type = entityToReplace.getType();
           BiomeType biome = entityToReplace.getLocation().getBiome();
           int height = entityToReplace.getLocation().getPosition().getFloorY();

           Optional<MonsterBean> spawnCandidate = getEpicMonster(type,biome,height);
           if(spawnCandidate.isPresent()) {
               Location<World> location = entityToReplace.getLocation();
                  Brawlator.getLogger().info("location biome {} name={}",location.getBiome().getId(),location.getBiome().getName());
                  Entity  entity = monsterAction.createEntity(location,spawnCandidate.get());
                  event.getEntities().set(i,entity);
            }
        }
    }
    private Optional<MonsterBean> getEpicMonster(EntityType type, BiomeType biomeType, int height){
        List<MonsterBean> epicMonsterList = monsterAction.filterMonster(type,biomeType,height);
        Brawlator.getLogger().info("find entity of type={} biome={} height={} and get list of [{}]",type,biomeType,height,epicMonsterList);
        return fetcher.fetcher(epicMonsterList);
    }
}
