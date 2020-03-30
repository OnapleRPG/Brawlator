package com.onaple.brawlator.actions;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.MonsterLootManipulator;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.data.beans.table.LootTable;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterSpawnedBean;
import com.onaple.brawlator.data.dao.MonsterSpawnedDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import com.onaple.brawlator.probability.ProbabilityFetcher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Singleton
public class MonsterAction {

    private final Random random;

    public MonsterAction() {
        random = new Random();
    }

    @Inject
    ConfigurationHandler configurationHandler;

    @Inject
    ProbabilityFetcher probabilityFetcher;

    public boolean monsterExists(String monsterName) {
        Optional<MonsterBean> monsterOptional = configurationHandler.getMonsterList().stream().filter(m -> m.getName().equalsIgnoreCase(monsterName)).findAny();
        return monsterOptional.isPresent() || Sponge.getRegistry().getType(EntityType.class, monsterName).isPresent();
    }

    public void invokeMonster(Location location, String monsterName, int spawnerId) throws EntityTypeNotFoundException, MonsterNotFoundException {
        Optional<MonsterBean> monsterOptional = configurationHandler.getMonsterList().stream().filter(m -> m.getName().equalsIgnoreCase(monsterName)).findAny();
        if (monsterOptional.isPresent()) {
            MonsterBean monster = monsterOptional.get();

            Entity baseEntity = createEntity(location,monster);

            if (spawnerId != -1) {
                MonsterSpawnedDao.addMonsterSpawned(new MonsterSpawnedBean(spawnerId, baseEntity.getUniqueId(), baseEntity.getWorld().getName()));
            }
            location.spawnEntity(baseEntity);
        } else {
            Optional<EntityType> entityTypeOptional = Sponge.getRegistry().getType(EntityType.class, monsterName);
            if (!entityTypeOptional.isPresent()) {
                throw new MonsterNotFoundException(monsterName);
            }

            Entity baseEntity = location.createEntity(entityTypeOptional.get());

            if (spawnerId != -1) {
                MonsterSpawnedDao.addMonsterSpawned(new MonsterSpawnedBean(spawnerId, baseEntity.getUniqueId(), baseEntity.getWorld().getName()));
            }

            location.spawnEntity(baseEntity);
        }
    }

    public Entity createEntity(Location location, MonsterBean monster) {
        Entity entity = location.createEntity(monster.getType());
        entity.offer(Keys.DISPLAY_NAME, Text.of(monster.getName()));
        entity.offer(Keys.CUSTOM_NAME_VISIBLE,true);
        entity.offer(Keys.MAX_HEALTH, monster.getHp());
        entity.offer(Keys.HEALTH, monster.getHp());
        entity.offer(Keys.WALKING_SPEED, monster.getSpeed());
        entity.offer(Keys.ATTACK_DAMAGE, monster.getAttackDamage());
        entity.offer(Keys.KNOCKBACK_STRENGTH, monster.getKnockbackResistance());
        applyLoot(entity, monster);
        return entity;
    }

    public Map<EntityType, List<MonsterBean>> getMonsterBytype() {
        List<MonsterBean> monsters = configurationHandler.getMonsterList();
        return monsters.stream().collect(Collectors.groupingBy(monsterBean -> monsterBean.getType()));
    }

    private Entity applyLoot(Entity entity, MonsterBean monster) {
        Optional<LootTable> tableOptional = probabilityFetcher.fetcher(monster.getLootTable());
        Brawlator.getLogger().info("monster loot table {}",tableOptional);
        if (tableOptional.isPresent()) {
            List<Loot> loots = tableOptional.get().fetchLoots(random.nextDouble());
            Brawlator.getLogger().info("monster loot = [{}]",loots);
            MonsterLootManipulator monsterLootManipulator = entity.getOrCreate(MonsterLootManipulator.class).get();
            entity.offer(monsterLootManipulator);
            entity.offer(BrawlatorKeys.LOOT, loots);
        }
        return entity;
    }
}
