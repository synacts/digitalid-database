package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.property.value.ReadOnlyValueProperty;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.validation.annotations.type.Functional;
import net.digitalid.utility.validation.annotations.type.Mutable;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.property.Subject;

/**
 * This read-only property stores a value in the persistent database.
 * 
 * @see WritablePersistentValueProperty
 */
@ReadOnly(WritablePersistentValueProperty.class)
public interface ReadOnlyPersistentValueProperty<S extends Subject, V> extends ReadOnlyValueProperty<V, DatabaseException, ReadOnlyPersistentValueProperty.Observer<S, V>, ReadOnlyPersistentValueProperty<S, V>>, PersistentProperty<S, ValuePropertyEntry<S, V>, ReadOnlyPersistentValueProperty.Observer<S, V>> {
    
    /* -------------------------------------------------- Observer -------------------------------------------------- */
    
    /**
     * Objects that implement this interface can be used to {@link #register(net.digitalid.utility.property.Property.Observer) observe} {@link ReadOnlyPersistentValueProperty read-only persistent value properties}.
     */
    @Mutable
    @Functional
    public static interface Observer<S extends Subject, V> extends ReadOnlyValueProperty.Observer<V, DatabaseException, ReadOnlyPersistentValueProperty.Observer<S, V>, ReadOnlyPersistentValueProperty<S, V>> {}
    
    /* -------------------------------------------------- Getter -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @NonCapturable @Valid V get() throws DatabaseException;
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull ValuePropertyTable<S, V> getTable();
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    /**
     * Returns the time of the last modification.
     */
    @Pure
    @NonCommitting
    public @Nonnull Time getTime() throws DatabaseException;
    
}
