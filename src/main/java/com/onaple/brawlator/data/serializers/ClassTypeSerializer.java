package com.onaple.brawlator.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.brawlator.Brawlator;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ClassTypeSerializer implements TypeSerializer<Class> {
    @Override
    public @Nullable Class deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        String typeName = value.getString();
        try {
            return Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            Brawlator.getLogger().error("Class named {} does not exist",typeName, e);
        }
        return null;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Class obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }
}
