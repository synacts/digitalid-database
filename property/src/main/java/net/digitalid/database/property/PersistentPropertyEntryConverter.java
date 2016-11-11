package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.interfaces.Site;
import net.digitalid.database.property.value.PersistentValuePropertyEntryConverter;

/**
 * This class converts the {@link PersistentPropertyEntry entries} of the {@link PersistentPropertyTable property table}.
 * 
 * @see PersistentValuePropertyEntryConverter
 */
@Immutable
public abstract class PersistentPropertyEntryConverter<S extends Subject, N extends PersistentPropertyEntry<S>> extends RootClass implements Converter<N, @Nonnull Site> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    /**
     * Returns the property table to which this entry converter belongs.
     */
    @Pure
    public abstract @Nonnull PersistentPropertyTable<S, N> getPropertyTable();
    
}
