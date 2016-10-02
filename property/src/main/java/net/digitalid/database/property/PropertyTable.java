package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.Site;
import net.digitalid.database.property.value.ValuePropertyTable;
import net.digitalid.database.storage.Table;

/**
 * A property table belongs to a {@link SubjectModule subject module} and stores the {@link PropertyEntry property entries}.
 * 
 * @see ValuePropertyTable
 */
@Immutable
public abstract class PropertyTable<S extends Subject, N extends PropertyEntry<S>> extends Table<N, Site> {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull SubjectModule<S> getParentModule();
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PropertyEntryConverter<S, N> getEntryConverter();
    
}
