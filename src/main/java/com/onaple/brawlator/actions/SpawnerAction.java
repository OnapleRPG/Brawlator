package com.onaple.brawlator.actions;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.onaple.brawlator.data.beans.SpawnerBean;
import com.onaple.brawlator.data.dao.SpawnerDao;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SpawnerAction {
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
        spawners = SpawnerDao.getSpawners();
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

    public void createSpawner(Vector3d position, int spawnerConfigId) {
        SpawnerDao.addSpawner(new SpawnerBean(new Vector3i((int)position.getX(), (int)position.getY(), (int)position.getZ()), spawnerConfigId));
    }

    public int removeSpawnersAround(Vector3d position) {
        List<SpawnerBean> spawnersToDelete = SpawnerDao.getSpawnersAround(new Vector3i((int)Math.round(position.getX()), (int)Math.round(position.getY()), (int)Math.round(position.getZ())));
        SpawnerDao.deleteSpawners(spawnersToDelete);
        return spawnersToDelete.size();
    }
}
