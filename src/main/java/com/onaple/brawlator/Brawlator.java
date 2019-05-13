package com.onaple.brawlator;

import com.onaple.brawlator.commands.MonstroVisionCommand;
import com.onaple.brawlator.commands.SpawnerCreateCommand;
import com.onaple.brawlator.commands.SpawnerDeleteCommand;
import com.onaple.brawlator.data.dao.SpawnerDao;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
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
    private static final String BRAWLATOR_PERMISSION = "brawlator.command";
    private static final String SPAWNER_PERMISSION = "brawlator.command.spawner";
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

        SpawnerDao.createTableIfNotExist();

        CommandSpec monstrovisionCommand = CommandSpec.builder()
                .description(Text.of("Enable or disable the monstrovision, to be able to see Brawlator spawners"))
                .permission(MONSTROVISION_PERMISSION)
                .arguments(
                        GenericArguments.optional(GenericArguments.bool(Text.of("value")))
                )
                .executor(new MonstroVisionCommand()).build();

        CommandSpec spawnerCreateCommand = CommandSpec.builder()
                .description(Text.of("Create a Brawlator spawner"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("spawnerConfigId")))
                )
                .executor(new SpawnerCreateCommand()).build();

        CommandSpec spawnerDeleteCommand = CommandSpec.builder()
                .description(Text.of("Delete a Brawlator spawner around you"))
                .executor(new SpawnerDeleteCommand()).build();

        CommandSpec spawnerCommand = CommandSpec.builder()
                .description(Text.of("Spawner related commands"))
                .permission(SPAWNER_PERMISSION)
                .child(spawnerCreateCommand, "create")
                .child(spawnerDeleteCommand, "delete")
                .build();

        CommandSpec brawlatorCommand = CommandSpec.builder()
                .description(Text.of("Brawlator commands - custom spawner utilities"))
                .permission(BRAWLATOR_PERMISSION)
                .child(spawnerCommand, "spawner")
                .child(monstrovisionCommand, "view")
                .build();
        Sponge.getCommandManager().register(this, brawlatorCommand, "brawlator");

        Task.builder().execute(spawnerAction::showSpawnersParticles)
                .delay(1, TimeUnit.SECONDS).interval(1, TimeUnit.SECONDS)
                .name("Task showing spawners particles.").submit(this);

        spawnerAction.updateSpawners();

		getLogger().info("BRAWLATOR initialized.");
	}
}
