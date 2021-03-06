package com.onaple.brawlator.actions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.data.beans.SpawnerBean;
import com.onaple.brawlator.data.beans.SpawnerTypeBean;
import com.onaple.brawlator.data.dao.MonsterSpawnedDao;
import com.onaple.brawlator.data.dao.SpawnerDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import com.onaple.brawlator.exceptions.SpawnerTypeNotFoundException;
import com.onaple.brawlator.utils.SpawnerBuilder;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Singleton
public class SpawnerAction {


    @Inject
    private SpawnerBuilder spawnerBuilder;

    @Inject
    private ConfigurationHandler configurationHandler;

    @Inject SpawnerDao spawnerDao;

    private Collection<Player> spawnerViewers = new ArrayList<>();
    private Collection<SpawnerBean> spawners = new ArrayList<>();

    public boolean isPlayerRegisteredToVision(Player player) {
        return spawnerViewers.contains(player);
    }
    public void registerPlayerToVision(Player player) {
        spawnerViewers.add(player);
    }
    public void unregisterPlayerToVision(Player player) {
        spawnerViewers.remove(player);
    }

    public void updateSpawners() {
        spawners = spawnerDao.getSpawners();
    }

    public void showSpawnersParticles() {
        ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.MOBSPAWNER_FLAMES).build();
        spawnerViewers.forEach(player -> {
            spawners.forEach(spawner -> {
                Vector3i position = spawner.getPosition();
                player.spawnParticles(particle, new Vector3d(position.getX(), position.getY(), position.getZ()));
            });
        });
    }

    public void createSpawner(Vector3d position, String worldName, String spawnerTypeName, String monsterName) throws SpawnerTypeNotFoundException, MonsterNotFoundException {
        if (!spawnerTypeExists(spawnerTypeName)) {
            throw new SpawnerTypeNotFoundException(spawnerTypeName);
        }
        if (!Brawlator.getMonsterAction().monsterExists(monsterName)) {
            throw new MonsterNotFoundException(monsterName);
        }
        spawnerDao.addSpawner(spawnerBuilder.buildSpawner(new Vector3i((int)position.getX(), (int)position.getY(), (int)position.getZ()), worldName, spawnerTypeName, monsterName));
        updateSpawners();
    }

    private boolean spawnerTypeExists(String spawnerTypeName) {
        Optional<SpawnerTypeBean> spawnerTypeBeanOptional = configurationHandler.getSpawnerTypeList().stream().filter(m -> m.getName().toLowerCase().equals(spawnerTypeName.toLowerCase())).findAny();
        return spawnerTypeBeanOptional.isPresent();
    }

    public int removeSpawnersAround(Vector3d position) {
        List<SpawnerBean> spawnersToDelete = spawnerDao.getSpawnersAround(new Vector3i((int)Math.round(position.getX()), (int)Math.round(position.getY()), (int)Math.round(position.getZ())));
        spawnerDao.deleteSpawners(spawnersToDelete);
        return spawnersToDelete.size();
    }

    public void invokeSpawnerMonsters() {
        spawners.forEach(spawner -> {
            if (spawner.getSpawnerType() != null && spawner.getWorld() != null) {
                // Choosing random position
                int spawnX = (int) Math.round(spawner.getPosition().getX() + Math.random() * spawner.getSpawnerType().getMaxSpawnRange()*2 - spawner.getSpawnerType().getMaxSpawnRange());
                int spawnZ = (int) Math.round(spawner.getPosition().getZ() + Math.random() * spawner.getSpawnerType().getMaxSpawnRange()*2 - spawner.getSpawnerType().getMaxSpawnRange());
                // Checking rate limitation
                if (((new Date()).getTime() - spawner.getLastSpawn().getTime()) / 1000 >= spawner.getSpawnerType().getRate()) {
                    if (MonsterSpawnedDao.getMonstersBySpawner(spawner.getId()).size() < spawner.getSpawnerType().getQuantityMax()) {
                        // Invoking
                        Brawlator.getMonsterAction().invokeMonster(spawner.getWorld().getLocation(new Vector3i(spawnX, spawner.getPosition().getY(), spawnZ)), spawner.getMonster(), spawner.getId());
                        spawner.setLastSpawn(new Date());
                    }
                }
            }
        });
    }

    public void deleteNonExistingMonsters() {
        MonsterSpawnedDao.getMonstersSpawned().forEach(monster -> {
            Sponge.getServer().getWorld(monster.getWorldName()).ifPresent(world -> {
                if (!world.getEntity(monster.getUuid()).isPresent()) {
                    MonsterSpawnedDao.deleteMonsterByUuid(monster.getUuid().toString());
                }
            });
        });
    }

    public void despawnUndisciplinedMonsters() {
        MonsterSpawnedDao.getMonstersSpawned().forEach(monster -> {
            spawnerDao.getSpawners().stream().filter(spawner -> spawner.getId() == monster.getSpawnerId()).findAny().ifPresent(spawner -> {
                spawner.getWorld().getEntity(monster.getUuid()).ifPresent(entity -> {
                    Vector3d entityPosition = entity.getLocation().getPosition();
                    Vector3i spawnerPosition = spawner.getPosition();
                    if (spawner.getSpawnerType().getMaxRoamRange() > 0 && new Vector3d(spawnerPosition.getX(), spawnerPosition.getY(), spawnerPosition.getZ()).distance(entityPosition) > spawner.getSpawnerType().getMaxRoamRange()) {
                        ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.CLOUD).quantity(10).build();
                        spawner.getWorld().getPlayers().forEach(player -> player.spawnParticles(particle, entityPosition.add(0, 1, 0)));
                        Task.builder().execute(() -> this.killUndisciplinedMonster(entity, spawner.getWorld()))
                                .delay(5, TimeUnit.SECONDS)
                                .name("Task despawning a given monster because he's too far away").submit(Brawlator.getInstance());
                    }
                });
            });
        });
    }

    private void killUndisciplinedMonster(Entity entity, World world) {
        if (entity != null && world.getEntity(entity.getUniqueId()).isPresent()) {
            ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.CLOUD).quantity(15).build();
            world.getPlayers().forEach(player -> player.spawnParticles(particle, entity.getLocation().getPosition().add(0, 1, 0)));
            MonsterSpawnedDao.deleteMonsterByUuid(entity.getUniqueId().toString());
            entity.remove();
        }
    }
}
