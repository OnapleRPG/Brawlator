package com.onaple.brawlator.data.manipulators;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.entity.Entity;

@ConfigSerializable
public interface MonsterAdditionalModifiers extends Comparable<MonsterAdditionalModifiers> {
    /**
     * Return the Key
     * @return
     */
    Key getKey();

    /**
     * Return the name of the Third Party
     * @return
     */
    String getName();

    /**
     * get the dataManipulator to set to the item
     * @return
     */
    DataManipulator<?,?> constructDataManipulator();

    /**
     * Apply the data to the item and update the lore
     * @param entity The Entity you want to apply the data
     */
    default void apply(Entity entity) {
        entity.offer(constructDataManipulator());
    }
}
