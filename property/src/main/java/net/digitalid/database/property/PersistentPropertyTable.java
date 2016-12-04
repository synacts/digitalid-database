package net.digitalid.database.property;

import net.digitalid.database.interfaces.Subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.interfaces.Site;
import net.digitalid.database.property.value.PersistentValuePropertyTable;
import net.digitalid.database.storage.Table;

/**
 * A property table belongs to a {@link SubjectModule subject module} and stores the {@link PersistentPropertyEntry property entries}.
 * 
 * @see PersistentValuePropertyTable
 */
@Immutable
public interface PersistentPropertyTable<S extends Subject, N extends PersistentPropertyEntry<S>> extends Table<N, Site> {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SubjectModule<S> getParentModule();
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentPropertyEntryConverter<S, N> getEntryConverter();
    
}
