package com.onaple.brawlator;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.actions.MonsterAction;
import com.onaple.brawlator.actions.SpawnerAction;
import com.onaple.brawlator.commands.InvokeCommand;
import com.onaple.brawlator.commands.ViewCommand;
import com.onaple.brawlator.commands.SpawnerCreateCommand;
import com.onaple.brawlator.commands.SpawnerDeleteCommand;
import com.onaple.brawlator.commands.elements.MonsterElement;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.manipulators.MonsterExperienceAmountManipulator;
import com.onaple.brawlator.data.manipulators.MonsterLootManipulator;
import com.onaple.brawlator.data.beans.GlobalConfig;
import com.onaple.brawlator.data.beans.table.LootTable;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.data.dao.MonsterSpawnedDao;
import com.onaple.brawlator.data.dao.SpawnerDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.data.serializers.LootSerializer;
import com.onaple.brawlator.data.serializers.LootTableSerializer;
import com.onaple.brawlator.events.DynamicEntityEventGenerator;
import com.onaple.brawlator.listeners.LootEventListener;
import com.onaple.brawlator.listeners.MonsterLifeCycleListener;
import com.onaple.brawlator.listeners.NaturalSpawnListener;
import com.onaple.brawlator.events.BrawlatorEntityDiedEvent;
import com.onaple.brawlator.probability.ProbabilityFetcher;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.script.ScriptException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
@Plugin(id = "brawlator", name = "Brawlator", version = "1.1.1", description = "Custom monster for more interesting fight",dependencies = {@Dependency(id="itemizer",optional = true)})
public class Brawlator {
    private static final String BRAWLATOR_PERMISSION = "brawlator.command";
    private static final String SPAWNER_PERMISSION = "brawlator.command.spawner";
    private static final String VISION_PERMISSION = "brawlator.command.vision";
    private static final String INVOKE_PERMISSION = "brawlator.command.invoke";

    private static Brawlator instance;

    public Brawlator() {
    }

    public static Brawlator getInstance() {
        return instance;
    }

    private static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        Brawlator.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static ConfigurationHandler configurationHandler;

    public static ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    @Inject
    public void setConfigurationHandler(ConfigurationHandler configurationHandler) {
        Brawlator.configurationHandler = configurationHandler;
    }

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    @Inject
    private PluginContainer pluginContainer;


    private static SpawnerAction spawnerAction;

    private static GlobalConfig globalConfig;

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    @Inject
    private void setSpawnerAction(SpawnerAction spawnerAction) {
        Brawlator.spawnerAction = spawnerAction;
    }

    public static SpawnerAction getSpawnerAction() {
        return spawnerAction;
    }


    private static MonsterAction monsterAction;

    @Inject
    public void setMonsterAction(MonsterAction monsterAction) {
        Brawlator.monsterAction = monsterAction;
    }

    public static MonsterAction getMonsterAction() {
        return monsterAction;
    }

    @Inject
    private PluginManager pluginManager;

    @Inject
    SpawnerDao spawnerDao;

    @Inject
    private ProbabilityFetcher probabilityFetcher;

    @Inject
    private ScriptManager scriptManager;

    @Inject MonsterLifeCycleListener monsterLifeCycleListener;
    @Inject private DynamicEntityEventGenerator dynamicEntityDieEventGenerator;


    @Listener
    public void preInit(GamePreInitializationEvent e) {
        Sponge.getEventManager().registerListeners(this, new LootEventListener());
        Sponge.getEventManager().registerListeners(this, monsterLifeCycleListener);
    }

    @Listener
    public void onKeyRegistration(GameRegistryEvent.Register<Key<?>> event){
        new BrawlatorKeys();
        event.register(BrawlatorKeys.LOOT);
        event.register(BrawlatorKeys.EXPERIENCE);
    }

    @Listener
    public void onDataRegistration(GameRegistryEvent.Register<DataRegistration<?, ?>> event) {
        DataRegistration.builder()
                .name("Monster loot")
                .id("monster.loot")
                .dataClass(MonsterLootManipulator.class)
                .immutableClass(MonsterLootManipulator.Immutable.class)
                .builder(new MonsterLootManipulator.Builder())
                .build();
        DataRegistration.builder()
                .name("Monster experience")
                .id("monster.xp.amount")
                .dataClass(MonsterExperienceAmountManipulator.class)
                .immutableClass(MonsterExperienceAmountManipulator.Immutable.class)
                .builder(new MonsterExperienceAmountManipulator.Builder())
                .build();
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws IllegalAccessException, InstantiationException, IOException, ScriptException {

        Brawlator.instance = this;

        spawnerDao.createTableIfNotExist();
        MonsterSpawnedDao.createTableIfNotExist();

        CommandSpec invokeCommand = CommandSpec.builder()
                .description(Text.of("Invoke a monster"))
                .permission(INVOKE_PERMISSION)
                .arguments(
                        new MonsterElement(Text.of("monster")),
                        GenericArguments.optional(GenericArguments.vector3d(Text.of("position")))
                )
                .executor(new InvokeCommand()).build();

        CommandSpec monstrovisionCommand = CommandSpec.builder()
                .description(Text.of("Enable or disable the monstrovision, to be able to see Brawlator spawners"))
                .permission(VISION_PERMISSION)
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
        Task.builder().execute(spawnerAction::deleteNonExistingMonsters)
                .delay(30, TimeUnit.SECONDS).interval(30, TimeUnit.SECONDS)
                .name("Task deleting non existing monsters spawned from spawner from database").submit(this);
        Task.builder().execute(spawnerAction::despawnUndisciplinedMonsters)
                .delay(15, TimeUnit.SECONDS).interval(15, TimeUnit.SECONDS)
                .name("Task despawning monsters that go too far away from their spawner").submit(this);
        Task.builder().execute(spawnerAction::invokeSpawnerMonsters)
                .delay(5, TimeUnit.SECONDS).interval(5, TimeUnit.SECONDS)
                .name("Task invoking the monsters on every spawners.").submit(this);


        initDefaultConfig("global.conf");
        Brawlator.globalConfig = getConfigurationHandler().loadGlobalConfig(configDir + "/brawlator/global.conf");

        getLogger().info("{} loot tables loaded from configuration.", loadLoot());
        getLogger().info("{} monsters loaded from configuration.", loadMonsters());
        getLogger().info("{} spawners types loaded from configuration.", loadSpawnerTypes());


        if (Brawlator.getGlobalConfig().isEnableNaturalSpawning()) {
            getLogger().info("Enabled natural spawning of configured monsters.");
            Sponge.getEventManager().registerListeners(this, new NaturalSpawnListener(monsterAction,probabilityFetcher));
        } else {
            getLogger().info("Natural spawning of configured monsters disabled. To enable it edit global.conf > enableNaturalSpawning");
        }
        spawnerAction.updateSpawners();


        EventListener<BrawlatorEntityDiedEvent> listener = new MonsterDiedListener();
        Sponge.getEventManager().registerListener(this, BrawlatorEntityDiedEvent.class, listener);


        configurationHandler.getMonsterList().forEach(monsterBean -> {
            try {
                Object monsterEvent = scriptManager.load(monsterBean);
                getLogger().info("create listener {}",monsterEvent);
                Sponge.getEventManager().registerListeners(this, monsterEvent);
            } catch (Exception e) {
                getLogger().error("error while loading script", e);
            }


        });
        getLogger().info("BRAWLATOR initialized.");
    }


    private int loadMonsters() {
        initDefaultConfig("monsters.conf");
        try {
            TypeSerializerCollection serializers = TypeSerializerCollection.defaults().newChild();
            serializers.register(TypeToken.of(LootTable.class), new LootTableSerializer());
            ConfigurationOptions options = ConfigurationOptions.defaults().withSerializers(serializers);
            CommentedConfigurationNode configurationNode = configurationHandler.loadConfiguration(configDir + "/brawlator/monsters.conf",
                    options);
            return configurationHandler.readMonstersConfiguration(configurationNode);
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Could not read monsters configuration.",e);
            return 0;
        }
    }

    private int loadSpawnerTypes() {
        initDefaultConfig("spawners.conf");
        try {
            CommentedConfigurationNode configurationNode = configurationHandler.loadConfiguration(configDir + "/brawlator/spawners.conf",
                    ConfigurationOptions.defaults());
            return configurationHandler.readSpawnerTypesConfiguration(configurationNode);
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Could not read spawners types configuration.");
            return 0;
        }
    }

    private int loadLoot() {
        initDefaultConfig("loot.conf");
        try {
            TypeSerializerCollection serializers = TypeSerializerCollection.defaults().newChild();
            serializers.register(TypeToken.of(Loot.class), new LootSerializer());
            ConfigurationOptions options = ConfigurationOptions.defaults().withSerializers(serializers);
            CommentedConfigurationNode configurationNode = configurationHandler.loadConfiguration(configDir + "/brawlator/loot.conf", options);
            return configurationHandler.readLootTableConfiguration(configurationNode);
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Could not read spawners types configuration.", e);
            return 0;
        }
    }

    private void initDefaultConfig(String path) {
        if (Files.notExists(Paths.get(configDir + "/brawlator/" + path))) {

            Optional<Asset> defaultConfigFile = pluginContainer.getAsset(path);
            getLogger().info("No config file set for {}. Default config will be loaded", path);
            if (defaultConfigFile.isPresent()) {
                try {
                    defaultConfigFile.get().copyToDirectory(Paths.get(configDir + "/brawlator/"));
                } catch (IOException e) {
                    logger.error("Error while setting default configuration : {}", e.getMessage());
                }
            } else {
                logger.warn("{} config not found", path);
            }
        }
    }

    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        MonsterSpawnedDao.deleteMonsterByUuid(event.getTargetEntity().getUniqueId().toString());
    }

    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        MonsterSpawnedDao.getMonstersSpawned().forEach(monster -> {
            Sponge.getServer().getWorld(monster.getWorldName()).ifPresent(world -> {
                if (world.getEntity(monster.getUuid()).isPresent()) {
                    world.getEntity(monster.getUuid()).get().remove();
                }
            });
        });
        MonsterSpawnedDao.clearTable();
    }
}
