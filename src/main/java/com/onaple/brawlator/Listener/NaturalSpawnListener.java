package com.onaple.brawlator.Listener;

import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.probability.ProbabilityFetcher;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collections;
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
    public void OnNaturalSpawn(SpawnEntityEvent event){

        if(!event.getCause().root().getClass().getName().contains("net.minecraft.world.WorldServer")){
            return;
        }

       for(int i = 0; i< event.getEntities().size(); i++) {
            EntityType type = event.getEntities().get(i).getType();

           Optional<MonsterBean> spawnCandidate = getEpicMonster(type);
           if(spawnCandidate.isPresent()) {
               Location<World> location = event.getEntities().get(i).getLocation();
                  Entity  entity = monsterAction.createEntity(location,spawnCandidate.get());
                  event.getEntities().set(i,entity);
            }
        }
    }
    private Optional<MonsterBean> getEpicMonster(EntityType type){
        List<MonsterBean> epicMonsterList = monsterAction.getMonsterBytype().getOrDefault(type, Collections.emptyList());
        return fetcher.fetcher(epicMonsterList);
    }
}
