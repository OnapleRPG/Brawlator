package com.onaple.brawlator.data.manipulators;

import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.manipulators.IdDataManipulator;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

import static com.onaple.brawlator.BrawlatorKeys.EXPERIENCE;

public class MonsterExperienceAmountManipulator extends AbstractSingleData<Integer,MonsterExperienceAmountManipulator, MonsterExperienceAmountManipulator.Immutable> {

    public MonsterExperienceAmountManipulator(Integer value) {
        super(EXPERIENCE, value);
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(EXPERIENCE, getValue());
    }

    @Override
    public Optional<MonsterExperienceAmountManipulator> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MonsterExperienceAmountManipulator> data_ = dataHolder.get(MonsterExperienceAmountManipulator.class);
        if (data_.isPresent()) {
            MonsterExperienceAmountManipulator data = data_.get();
            MonsterExperienceAmountManipulator finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MonsterExperienceAmountManipulator> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MonsterExperienceAmountManipulator> from(DataView view) {
        if (view.contains(ItemizerKeys.ITEM_ID.getQuery())) {
            Optional<Integer> idValue = view.getInt(EXPERIENCE.getQuery());
            idValue.ifPresent(this::setValue);
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public MonsterExperienceAmountManipulator copy() {
        return new MonsterExperienceAmountManipulator(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(EXPERIENCE.getQuery(),getValue());
    }

    public static class Immutable extends AbstractImmutableSingleData<Integer,Immutable,MonsterExperienceAmountManipulator>{

        protected Immutable(Integer value) {
            super(EXPERIENCE, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(EXPERIENCE, getValue()).asImmutable();
        }

        @Override
        public MonsterExperienceAmountManipulator asMutable() {
            return new MonsterExperienceAmountManipulator(getValue());
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(EXPERIENCE.getQuery(),getValue());
        }
    }
    public static class Builder extends AbstractDataBuilder<MonsterExperienceAmountManipulator> implements DataManipulatorBuilder<MonsterExperienceAmountManipulator, Immutable> {
        public Builder() {
            super(MonsterExperienceAmountManipulator.class, 0);
        }

        @Override
        public MonsterExperienceAmountManipulator create() {
            return new MonsterExperienceAmountManipulator(0);
        }

        @Override
        public Optional<MonsterExperienceAmountManipulator> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MonsterExperienceAmountManipulator> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }
}
