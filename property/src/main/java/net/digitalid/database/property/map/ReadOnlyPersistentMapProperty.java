package net.digitalid.database.property.map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.property.map.ReadOnlyMapProperty;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.property.Subject;

/**
 * This read-only property stores a map of key-value pairs in the persistent database.
 * 
 * @see WritablePersistentMapProperty
 * @see ReadOnlyPersistentSimpleMapProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentMapProperty.class)
public interface ReadOnlyPersistentMapProperty<S extends Subject, K, V, R extends ReadOnlyMap<@Nonnull @Valid("key") K, @Nonnull @Valid V>> extends ReadOnlyMapProperty<K, V, R, DatabaseException, ReadOnlyPersistentMapProperty.Observer<S, K, V, R>, ReadOnlyPersistentMapProperty<S, K, V, R>>, PersistentProperty<S, PersistentMapPropertyEntry<S, K, V>, ReadOnlyPersistentMapProperty.Observer<S, K, V, R>> {
    
    /* -------------------------------------------------- Observer -------------------------------------------------- */
    
    /**
     * Objects that implement this interface can be used to {@link #register(net.digitalid.utility.property.Property.Observer) observe} {@link ReadOnlyPersistentMapProperty read-only persistent map properties}.
     */
    @Mutable
    @Functional
    public static interface Observer<S extends Subject, K, V, R extends ReadOnlyMap<@Nonnull @Valid("key") K, @Nonnull @Valid V>> extends ReadOnlyMapProperty.Observer<K, V, R, DatabaseException, ReadOnlyPersistentMapProperty.Observer<S, K, V, R>, ReadOnlyPersistentMapProperty<S, K, V, R>> {}
    
    /* -------------------------------------------------- Getters -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @Nonnull @NonFrozen R get() throws DatabaseException;
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Nullable @Valid V get(@NonCaptured @Unmodified @Nonnull @Valid("key") K key) throws DatabaseException;
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentMapPropertyTable<S, K, V, ?, ?> getTable();
    
}
