package com.onaple.brawlator;

import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.actions.SpawnerAction;
import com.onaple.brawlator.commands.InvokeCommand;
import com.onaple.brawlator.commands.ViewCommand;
import com.onaple.brawlator.commands.SpawnerCreateCommand;
import com.onaple.brawlator.commands.SpawnerDeleteCommand;
import com.onaple.brawlator.data.dao.SpawnerDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
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

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    private static SpawnerAction spawnerAction;
    public static SpawnerAction getSpawnerAction() {
        return spawnerAction;
    }

    private static MonsterAction monsterAction;
    public static MonsterAction getMonsterAction() {
        return monsterAction;
    }

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
        spawnerAction = new SpawnerAction();
        monsterAction = new MonsterAction();

        SpawnerDao.createTableIfNotExist();

        CommandSpec invokeCommand = CommandSpec.builder()
                .description(Text.of("Invoke a monster"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
                        GenericArguments.optional(GenericArguments.vector3d(Text.of("position")))
                )
                .executor(new InvokeCommand()).build();

        CommandSpec monstrovisionCommand = CommandSpec.builder()
                .description(Text.of("Enable or disable the monstrovision, to be able to see Brawlator spawners"))
                .permission(MONSTROVISION_PERMISSION)
                .arguments(
                        GenericArguments.optional(GenericArguments.bool(Text.of("value")))
                )
                .executor(new ViewCommand()).build();

        CommandSpec spawnerCreateCommand = CommandSpec.builder()
                .description(Text.of("Create a Brawlator spawner"))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("spawnerType"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("monsterName")))
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
                .child(invokeCommand, "invoke")
                .child(spawnerCommand, "spawner")
                .child(monstrovisionCommand, Arrays.asList("view", "vision"))
                .build();
        Sponge.getCommandManager().register(this, brawlatorCommand, "brawlator");

        Task.builder().execute(spawnerAction::showSpawnersParticles)
                .delay(1, TimeUnit.SECONDS).interval(1, TimeUnit.SECONDS)
                .name("Task showing spawners particles.").submit(this);
        Task.builder().execute(spawnerAction::invokeSpawnerMonsters)
                .delay(5, TimeUnit.SECONDS).interval(5, TimeUnit.SECONDS)
                .name("Task invoking the monsters on every spawners.").submit(this);

        getLogger().info(loadMonsters() + " monsters loaded.");
        getLogger().info(loadSpawnerTypes() + " spawners types loaded.");

        spawnerAction.updateSpawners();

		getLogger().info("BRAWLATOR initialized.");
	}

	private int loadMonsters() {
        initDefaultConfig("monsters.conf");
        try {
            return ConfigurationHandler.readMonstersConfiguration(ConfigurationHandler.loadConfiguration(configDir + "/brawlator/monsters.conf"));
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Could not read monsters configuration.");
            return 0;
        }
    }

    private int loadSpawnerTypes() {
        initDefaultConfig("spawners.conf");
        try {
            return ConfigurationHandler.readSpawnerTypesConfiguration(ConfigurationHandler.loadConfiguration(configDir + "/brawlator/spawners.conf"));
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Could not read spawners types configuration.");
            return 0;
        }
    }

    private void initDefaultConfig(String path) {
        if (Files.notExists(Paths.get(configDir + "/brawlator/" + path))) {
            PluginContainer pluginInstance = Sponge.getPluginManager().getPlugin("brawlator").orElse(null);
            if (pluginInstance != null) {
                Optional<Asset> monstersDefaultConfigFile = pluginInstance.getAsset(path);
                getLogger().info("No config file set for {}. Default config will be loaded",path);
                if (monstersDefaultConfigFile.isPresent()) {
                    try {
                        monstersDefaultConfigFile.get().copyToDirectory(Paths.get(configDir + "/brawlator/"));
                    } catch (IOException e) {
                        logger.error("Error while setting default configuration : {}", e.getMessage());
                    }
                } else {
                    logger.warn("Item default config not found");
                }
            }
        }
    }
}
