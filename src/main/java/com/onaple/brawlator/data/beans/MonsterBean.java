package com.onaple.brawlator.data.beans;

import com.onaple.brawlator.data.beans.table.LootTable;
import com.onaple.brawlator.data.manipulators.MonsterAdditionalModifiers;
import com.onaple.brawlator.probability.Probable;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@ConfigSerializable
public class MonsterBean implements Probable {
    /**
     * Monster name displayed above him
     */
    @Setting(value="name")
    private String name;
    /**
     * Monster type (race)
     */
    @Setting(value="type")
    private EntityType type;
    /**
     * Monster HP
     */
    @Setting(value="hp")
    private double hp;
    /**
     * Monster default speed
     */
    @Setting(value="speed")
    private double speed;
    /**
     * Monster attack damage
     */
    @Setting(value="attackDamage")
    private double attackDamage;
    /**
     * Monster knockback resistance
     */
    @Setting(value="knockbackResistance")
    private int knockbackResistance;

    @Setting(value = "naturalSpawn")
    private NaturalSpawnData naturalSpawn;

    @Setting(value = "pools")
    private List<LootTable> lootTable;


    public EquipmentBean getEquipments() {
        return equipments;
    }

    @Setting(value = "equipments")
    private EquipmentBean equipments;


    @Setting("additional")
    private Set<MonsterAdditionalModifiers> thirdParties = new TreeSet<>();

    @Setting("scripts")
    private List<String> scripts;

    private Map<String,Class<?>> events = new HashMap<>();

    public Map<String, Class<?>> getEvents() {
        return events;
    }

    public List<LootTable> getLootTable() {
        List<LootTable> recursiveTable = new ArrayList<>();
        recursiveTable.addAll(lootTable);
        recursiveTable.addAll(lootTable.stream().map(lootTable1 -> lootTable1.getInherits()).flatMap(lootTables -> lootTables.stream()).collect(Collectors.toList()));
    return recursiveTable;
    }

    public MonsterBean() {
        this.lootTable = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public EntityType getType() {
        return type;
    }
    public double getHp() {
        return hp;
    }
    public double getSpeed() { return speed; }
    public double getAttackDamage() { return attackDamage; }
    public int getKnockbackResistance() { return knockbackResistance; }
    public Set<MonsterAdditionalModifiers> getThirdParties() {
        return thirdParties;
    }
    public List<String> getScripts() {
        return scripts;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setType(EntityType type) {
        this.type = type;
    }
    public void setHp(double hp) { this.hp = hp; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setAttackDamage(double attackDamage) { this.attackDamage = attackDamage; }
    public void setKnockbackResistance(int knockbackResistance) { this.knockbackResistance = knockbackResistance; }
    public void setThirdParties(Set<MonsterAdditionalModifiers> thirdParties) {
        this.thirdParties = thirdParties;
    }
    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }

    @Override
    public double getProbability() {
        return naturalSpawn.getProbability();
    }

    public NaturalSpawnData getNaturalSpawn() {
        return naturalSpawn;
    }



    @Override
    public String toString() {
        return "MonsterBean{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", hp=" + hp +
                ", speed=" + speed +
                ", attackDamage=" + attackDamage +
                ", knockbackResistance=" + knockbackResistance +
                ", naturalSpawn=" + naturalSpawn +
                ", lootTable=" + lootTable +
                ", equipments=" + equipments +
                ", thirdParties=" + thirdParties +
                '}';
    }

}
