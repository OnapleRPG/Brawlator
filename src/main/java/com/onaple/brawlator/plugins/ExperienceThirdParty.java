package com.onaple.brawlator.plugins;

import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.manipulators.MonsterAdditionalModifiers;
import com.onaple.brawlator.data.manipulators.MonsterExperienceAmountManipulator;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;

public class ExperienceThirdParty implements MonsterAdditionalModifiers {

    @Setting("experience")
    private int experience;

    @Override
    public Key getKey() {
        return BrawlatorKeys.EXPERIENCE;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public DataManipulator<?, ?> constructDataManipulator() {
        return new MonsterExperienceAmountManipulator(experience);
    }

    @Override
    public int compareTo(MonsterAdditionalModifiers monsterAdditionalModifiers) {
        return 0;
    }
}
