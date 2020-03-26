package com.onaple.brawlator.events;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.SpawnEntityEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class NaturalSpawnListener {

    private final MonsterAction monsterAction;
    private final Random random;

    public NaturalSpawnListener(MonsterAction monsterAction) {
        this.monsterAction = monsterAction;
        random = new Random();
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
        double probability = random.nextDouble();
        return epicMonsterList.stream()
                .filter(monsterBean -> monsterBean.getNaturalSpawn() < probability)
                .max(Comparator.comparing(MonsterBean::getNaturalSpawn));
    }
}
