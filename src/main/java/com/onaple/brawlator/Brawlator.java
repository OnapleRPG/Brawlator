package com.onaple.brawlator;

import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import javax.inject.Inject;

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
		getLogger().info("BRAWLATOR initialized.");
	}
}
