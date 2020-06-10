package com.onaple.brawlator.actions;

import com.google.inject.internal.cglib.core.$AbstractClassGenerator;
import com.onaple.brawlator.Brawlator;
import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.beans.EquipmentBean;
import com.onaple.brawlator.data.beans.NaturalSpawnData;
import com.onaple.brawlator.data.manipulators.MonsterAdditionalModifiers;
import com.onaple.brawlator.data.manipulators.MonsterExperienceAmountManipulator;
import com.onaple.brawlator.data.manipulators.MonsterLootManipulator;
import com.onaple.brawlator.data.beans.loot.Loot;
import com.onaple.brawlator.data.beans.table.LootTable;
import com.onaple.brawlator.data.beans.MonsterBean;
import com.onaple.brawlator.data.beans.MonsterSpawnedBean;
import com.onaple.brawlator.data.dao.MonsterSpawnedDao;
import com.onaple.brawlator.data.handlers.ConfigurationHandler;
import com.onaple.brawlator.probability.ProbabilityFetcher;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.ArmorEquipable;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.biome.BiomeType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
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

    public void invokeMonster(Location location, MonsterBean monster, int spawnerId) {
            Entity baseEntity = createEntity(location,monster);

            if (spawnerId != -1) {
                MonsterSpawnedDao.addMonsterSpawned(new MonsterSpawnedBean(spawnerId, baseEntity.getUniqueId(), baseEntity.getWorld().getName()));
            }
            location.spawnEntity(baseEntity);
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
        Entity equippedEntity = applyEquipments(entity,monster.getEquipments());

        monster.getThirdParties().forEach(monsterAdditionalModifiers ->
                monsterAdditionalModifiers.apply(equippedEntity));

        return entity;
    }

    private Entity applyEquipments(Entity entity, EquipmentBean equipmentBean) {
        if (entity instanceof ArmorEquipable) {
            ArmorEquipable equipableEntity = (ArmorEquipable) entity;
                        equipableEntity.setItemInHand(HandTypes.MAIN_HAND, equipmentBean.getMainHand());
                        equipableEntity.setItemInHand(HandTypes.OFF_HAND, equipmentBean.getOffHand());
                        equipableEntity.setHelmet(equipmentBean.getHead());
                        equipableEntity.setChestplate(equipmentBean.getChest());
                        equipableEntity.setBoots(equipmentBean.getFoot());
                        equipableEntity.setLeggings(equipmentBean.getLegs());
            return (Entity) equipableEntity;
        }
        return  entity;
    }

    public List<MonsterBean> filterMonster(EntityType entityType, BiomeType biome, int currentHeight) {
        List<MonsterBean> monsters = configurationHandler.getMonsterList();
        return monsters.stream()
                .filter(isTypeEquals(entityType))
                .filter(hasSpawnedInBiome(biome))
                .filter(hasSpawnedBelow(currentHeight))
                .collect(Collectors.toList());
    }

    private Predicate<MonsterBean> isTypeEquals(EntityType entityType){
        return m -> m.getType().equals(entityType);
    }
    private Predicate<MonsterBean> hasSpawnedInBiome(BiomeType biome){
         return  m -> {
             if(Objects.nonNull(m.getNaturalSpawn().getBiomeType())) {
                 return m.getNaturalSpawn().getBiomeType().equals(biome);
             }
             return true;
         };
    }
    private Predicate<MonsterBean> hasSpawnedBelow(int currentHeight){
       return m -> {
           if (m.getNaturalSpawn().getMaxHeight() > 0) {
               return m.getNaturalSpawn().getMaxHeight() >= currentHeight;
           }
           return true;
       };
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
