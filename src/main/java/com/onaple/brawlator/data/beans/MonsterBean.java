package com.onaple.brawlator.data.beans;

import com.onaple.brawlator.data.beans.table.LootTable;
import com.onaple.brawlator.probability.Probable;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
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

    public void setNaturalSpawn(double naturalSpawn) {
        this.naturalSpawn = naturalSpawn;
    }

    @Setting(value = "naturalSpawn")
    private double naturalSpawn;

    @Setting(value = "pools")
    private List<LootTable> lootTable;


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

    @Override
    public double getProbability() {
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
                '}';
    }
}
