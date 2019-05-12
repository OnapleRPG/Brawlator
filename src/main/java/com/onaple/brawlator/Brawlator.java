package com.onaple.brawlator;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Plugin(id = "brawlator", name = "Brawlator", version = "0.1.0")
public class Brawlator {
    private static Logger logger;
    @Inject
    private void setLogger(Logger logger) {
        Brawlator.logger = logger;
    }
    public static Logger getLogger() {
        return logger;
    }

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
//        SpawnerAction spawnerAction = new SpawnerAction();
//        Task.builder().execute(spawnerAction::showSpawnersParticles)
//                .delay(1, TimeUnit.SECONDS).interval(1, TimeUnit.SECONDS)
//                .name("Task showing spawners particles.").submit(this);

		getLogger().info("BRAWLATOR initialized.");
	}
}
