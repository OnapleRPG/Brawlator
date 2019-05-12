package com.onaple.brawlator;

import com.flowpowered.math.vector.Vector3d;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;

public class SpawnerAction {
    private Collection<Player> spawnerViewers;

    public void showSpawnersParticles() {
        spawnerViewers = Sponge.getServer().getOnlinePlayers();
        ParticleEffect particle = ParticleEffect.builder().type(ParticleTypes.MOBSPAWNER_FLAMES).build();
        spawnerViewers.forEach(player -> {
            player.spawnParticles(particle, new Vector3d(0, 100, 0));
        });
    }
}
