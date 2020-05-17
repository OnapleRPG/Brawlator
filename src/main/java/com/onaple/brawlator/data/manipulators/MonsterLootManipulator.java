package com.onaple.brawlator.data.manipulators;

import com.onaple.brawlator.BrawlatorKeys;
import com.onaple.brawlator.data.beans.loot.Loot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableListData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MonsterLootManipulator extends AbstractListData<Loot, MonsterLootManipulator, MonsterLootManipulator.Immutable> {


    protected MonsterLootManipulator(List<Loot> value) {
        super(value, BrawlatorKeys.LOOT);
    }


    @Override
    public Optional<MonsterLootManipulator> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<MonsterLootManipulator> data_ = dataHolder.get(MonsterLootManipulator.class);
        if (data_.isPresent()) {
            MonsterLootManipulator data = data_.get();
            MonsterLootManipulator finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<MonsterLootManipulator> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<MonsterLootManipulator> from(DataView view) {
        if (view.contains(BrawlatorKeys.LOOT.getQuery())) {
            Optional<List<Loot>> idValue = view.getSerializableList(BrawlatorKeys.LOOT.getQuery(),Loot.class);
            idValue.ifPresent(this::setValue);
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MonsterLootManipulator copy() {
        return new MonsterLootManipulator(getValue());
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
        return super.toContainer()
                .set(BrawlatorKeys.LOOT.getQuery(), getValue());
    }

    public static class Immutable extends AbstractImmutableListData<Loot, Immutable, MonsterLootManipulator> {


        protected Immutable(List<Loot> value) {
            super(value, BrawlatorKeys.LOOT);
        }


        @Override
        public MonsterLootManipulator asMutable() {
            return new MonsterLootManipulator(getValue());
        }

        @Override
        public int getContentVersion() {
            return 0;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(BrawlatorKeys.LOOT.getQuery(), getValue());

        }
    }
    public static class Builder extends AbstractDataBuilder<MonsterLootManipulator> implements DataManipulatorBuilder<MonsterLootManipulator, Immutable> {
        public Builder() {
            super(MonsterLootManipulator.class, 0);
        }

        @Override
        public MonsterLootManipulator create() {
            return new MonsterLootManipulator(new ArrayList<>());}

        @Override
        public Optional<MonsterLootManipulator> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<MonsterLootManipulator> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }
}
