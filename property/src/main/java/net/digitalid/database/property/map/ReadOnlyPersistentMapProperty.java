package net.digitalid.database.property.map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.property.map.ReadOnlyMapProperty;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.subject.Subject;

/**
 * This read-only property stores a map of key-value pairs in the persistent database.
 * 
 * @see WritablePersistentMapProperty
 * @see ReadOnlyPersistentSimpleMapProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentMapProperty.class)
public interface ReadOnlyPersistentMapProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable KEY, @Unspecifiable VALUE, @Unspecifiable READONLY_MAP extends ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> extends ReadOnlyMapProperty<KEY, VALUE, READONLY_MAP, DatabaseException, RecoveryException, PersistentMapObserver<SUBJECT, KEY, VALUE, READONLY_MAP>, ReadOnlyPersistentMapProperty<SUBJECT, KEY, VALUE, READONLY_MAP>>, PersistentProperty<SUBJECT, PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>, PersistentMapObserver<SUBJECT, KEY, VALUE, READONLY_MAP>> {
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @Nonnull @NonFrozen READONLY_MAP get() throws DatabaseException, RecoveryException;
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Nullable @Valid VALUE get(@NonCaptured @Unmodified @Nonnull @Valid("key") KEY key) throws DatabaseException, RecoveryException;
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentMapPropertyTable<?, SUBJECT, KEY, VALUE, ?, ?> getTable();
    
}
