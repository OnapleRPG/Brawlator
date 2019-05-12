package com.onaple.brawlator;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Collection;

public class SpawnerAction {
    private Collection<Player> spawnerViewers = new ArrayList<>();

    public boolean isPlayerRegisteredToVision(Player player) {
        return spawnerViewers.contains(player);
    }
    public void registerPlayerToVision(Player player) {
        spawnerViewers.add(player);
    }
    public void unregisterPlayerToVision(Player player) {
        spawnerViewers.remove(player);
    }

    public void showSpawnersParticles() {
        ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.MOBSPAWNER_FLAMES).build();
        spawnerViewers.forEach(player -> {
            player.spawnParticles(particle, new Vector3d(0, 100, 0));
        });
    }
}
