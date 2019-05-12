package com.onaple.brawlator;

import com.onaple.brawlator.commands.MonstroVisionCommand;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

@Plugin(id = "brawlator", name = "Brawlator", version = "0.1.0")
public class Brawlator {
    private static final String MONSTROVISION_PERMISSION = "brawlator.command.monstrovision";

    private static Logger logger;
    @Inject
    private void setLogger(Logger logger) {
        Brawlator.logger = logger;
    }
    public static Logger getLogger() {
        return logger;
    }

    private static SpawnerAction spawnerAction;
    public static SpawnerAction getSpawnerAction() {
        return spawnerAction;
    }

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
        spawnerAction = new SpawnerAction();

        CommandSpec monstrovisionCommand = CommandSpec.builder()
                .description(Text.of("Enable or disable the monstrovision, to be able to see Brawlator spawners"))
                .permission(MONSTROVISION_PERMISSION)
                .executor(new MonstroVisionCommand()).build();
        Sponge.getCommandManager().register(this, monstrovisionCommand, "monstrovision");

        Task.builder().execute(spawnerAction::showSpawnersParticles)
                .delay(1, TimeUnit.SECONDS).interval(1, TimeUnit.SECONDS)
                .name("Task showing spawners particles.").submit(this);

		getLogger().info("BRAWLATOR initialized.");
	}
}
