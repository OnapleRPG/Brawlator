package com.onaple.brawlator.actions;

import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.exceptions.EntityTypeNotFoundException;
import com.onaple.brawlator.exceptions.MonsterNotFoundException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;

import java.util.Optional;

public class MonsterAction {
    public void invokeMonster(Location location, String monsterName) throws EntityTypeNotFoundException, MonsterNotFoundException {
        Optional<MonsterBean> monsterOptional = ConfigurationHandler.getMonsterList().stream().filter(m -> m.getName().toLowerCase().equals(monsterName.toLowerCase())).findAny();
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

            location.spawnEntity(baseEntity);
        } else {
            Optional<EntityType> entityTypeOptional = Sponge.getRegistry().getType(EntityType.class, monsterName);
            if (!entityTypeOptional.isPresent()) {
                throw new MonsterNotFoundException(monsterName);
            }

            Entity baseEntity = location.createEntity(entityTypeOptional.get());
            location.spawnEntity(baseEntity);
        }
    }
}
