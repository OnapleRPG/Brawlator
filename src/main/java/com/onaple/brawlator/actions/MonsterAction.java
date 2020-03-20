package com.onaple.brawlator.actions;

import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.MonsterLootManipulator;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterSpawnedBean;
import com.onaple.brawlator.data.beans.loot.ItemTypeLoot;
import com.onaple.brawlator.data.dao.MonsterSpawnedDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class MonsterAction {

    public MonsterAction() {
    }

    @Inject
    ConfigurationHandler configurationHandler;

    public boolean monsterExists(String monsterName) {
        Optional<MonsterBean> monsterOptional = configurationHandler.getMonsterList().stream().filter(m -> m.getName().toLowerCase().equals(monsterName.toLowerCase())).findAny();
        return monsterOptional.isPresent() || Sponge.getRegistry().getType(EntityType.class, monsterName).isPresent();
    }

    public void invokeMonster(Location location, String monsterName, int spawnerId) throws EntityTypeNotFoundException, MonsterNotFoundException {
        Optional<MonsterBean> monsterOptional = configurationHandler.getMonsterList().stream().filter(m -> m.getName().toLowerCase().equals(monsterName.toLowerCase())).findAny();
        if (monsterOptional.isPresent()) {
            MonsterBean monster = monsterOptional.get();

            Optional<EntityType> entityTypeOptional = Sponge.getRegistry().getType(EntityType.class, monster.getType());
            if (!entityTypeOptional.isPresent()) {
                throw new EntityTypeNotFoundException(monster.getType());
            }

            Entity baseEntity = location.createEntity(entityTypeOptional.get());

            baseEntity.offer(Keys.MAX_HEALTH, monster.getHp());
            baseEntity.offer(Keys.HEALTH, monster.getHp());
            baseEntity.offer(Keys.WALKING_SPEED, monster.getSpeed());
            baseEntity.offer(Keys.ATTACK_DAMAGE, monster.getAttackDamage());
            baseEntity.offer(Keys.KNOCKBACK_STRENGTH, monster.getKnockbackResistance());
            MonsterLootManipulator monsterLootManipulator = baseEntity.getOrCreate(MonsterLootManipulator.class).get();
            baseEntity.offer(monsterLootManipulator);
            baseEntity.offer(BrawlatorKeys.LOOT, monster.getLootTable().getLoot());
            Brawlator.getLogger().info("loot {}", baseEntity.get(BrawlatorKeys.LOOT));
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
}
