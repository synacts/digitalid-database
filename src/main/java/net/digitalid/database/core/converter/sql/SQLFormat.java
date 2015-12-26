package net.digitalid.database.core.converter.sql;

import net.digitalid.database.core.converter.sql.serializer.ObjectSerializer;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.utility.collections.freezable.FreezableHashMap;
import net.digitalid.utility.collections.freezable.FreezableMap;
import net.digitalid.utility.system.converter.exceptions.SerializerException;
import net.digitalid.utility.system.converter.Format;
import net.digitalid.utility.system.converter.Serializer;
import net.digitalid.utility.system.converter.Deserializer;

public class SQLFormat implements Format<ValueCollector> {

    private static final FreezableMap<Class<?>, Serializer> typeSerializers = FreezableHashMap.get();
    private static final Serializer<ValueCollector> defaultSerializer = new ObjectSerializer();
    
    @Override
    public <T> Serializer<ValueCollector> getSerializer(Class<T> clazz) throws SerializerException {
        return null;
    }

    @Override
    public <T> Deserializer<ValueCollector, T> getDeserializer(ValueCollector encodedForm) {
        return null;
    }
    
}
