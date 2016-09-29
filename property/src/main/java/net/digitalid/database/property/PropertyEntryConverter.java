package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.value.ValuePropertyEntryConverter;

/**
 * This class converts the {@link PropertyEntry entries} of the {@link PropertyTable property table}.
 * 
 * @see ValuePropertyEntryConverter
 */
@Immutable
public abstract class PropertyEntryConverter<S extends Subject, N extends PropertyEntry<S>> extends RootClass implements Converter<N, Void> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    /**
     * Returns the property table to which this entry converter belongs.
     */
    @Pure
    public abstract @Nonnull PropertyTable<S, N> getPropertyTable();
    
}
