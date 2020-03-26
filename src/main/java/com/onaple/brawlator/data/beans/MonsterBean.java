package com.onaple.brawlator.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;

@ConfigSerializable
public class MonsterBean {
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

    public double getNaturalSpawn() {
        return naturalSpawn;
    }

    public void setNaturalSpawn(double naturalSpawn) {
        this.naturalSpawn = naturalSpawn;
    }

    @Setting(value = "naturalSpawn")
    private double naturalSpawn;

    public LootTable getLootTable() {
        return lootTable;
    }

    @Setting(value = "loot")
    private LootTable lootTable;

    public MonsterBean() {
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
    public void setType(EntityType _type) {
        this.type = _type;
    }
    public void setHp(double hp) { this.hp = hp; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setAttackDamage(double attackDamage) { this.attackDamage = attackDamage; }
    public void setKnockbackResistance(int knockbackResistance) { this.knockbackResistance = knockbackResistance; }
}
