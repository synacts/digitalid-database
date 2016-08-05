package net.digitalid.database.property;

import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * Description.
 */
@Immutable
public abstract class PropertyEntryConverter<O, V, T extends PropertyEntry<O, V>> implements Converter<T, Void> { // TODO: extends DynamicConverter
    
    
    
}
