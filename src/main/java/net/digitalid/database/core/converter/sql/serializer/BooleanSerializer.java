package net.digitalid.database.core.converter.sql.serializer;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.service.core.block.wrappers.value.BooleanWrapper;
import net.digitalid.service.core.converter.Format;
import net.digitalid.service.core.converter.Serializer;
import net.digitalid.utility.system.converter.exceptions.StoringException;

public class BooleanSerializer implements Serializer<ValueCollector> {
    
    @Override
    public ValueCollector store(Object object, Format<ValueCollector> format, String fieldName, String clazzName) throws StoringException {
        assert (object instanceof Boolean) : "The object is an instance of the boolean type.";
        
        Boolean value = (Boolean) object;
        
        BooleanWrapper.store(value, format.getValueCollector());
    }
    
}
