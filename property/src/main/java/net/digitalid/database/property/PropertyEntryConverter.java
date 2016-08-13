package net.digitalid.database.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * Description.
 */
@Immutable
public abstract class PropertyEntryConverter<O, V, E extends PropertyEntry<O, V>> implements Converter<E, @Nullable Object> {
    
    /* -------------------------------------------------- Converter -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the object.
     */
    @Pure
    public abstract @Nonnull Converter<O, @Nullable Object> getObjectConverter();
    
    /**
     * Returns the converter to convert and recover the value of the property.
     */
    @Pure
    public abstract @Nonnull Converter<V, @Nullable Object> getValueConverter();
    
}
