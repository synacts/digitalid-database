package net.digitalid.database.core.converter.sql.serializer;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.utility.system.converter.Converter;
import net.digitalid.utility.system.converter.Format;
import net.digitalid.utility.system.converter.Serializer;
import net.digitalid.utility.system.converter.exceptions.StoringException;

public class ObjectSerializer implements Serializer<ValueCollector> {
    
    @Override
    public ValueCollector store(Object object, Format<ValueCollector> format, String fieldName, String clazzName) throws StoringException {
        ValueCollector serializedObject = Converter.store(object, format, fieldName + "." + clazzName);
        return serializedObject;
    }
    
}
