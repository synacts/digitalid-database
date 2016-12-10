package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.concurrency.exceptions.ReentranceException;
import net.digitalid.utility.property.Property;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.value.ReadOnlyPersistentValueProperty;
import net.digitalid.database.subject.Subject;

/**
 * A persistent property belongs to a {@link Subject subject} and stores its values in the database with the subject used as the key.
 * 
 * @see ReadOnlyPersistentValueProperty
 */
@Mutable
@ThreadSafe
public interface PersistentProperty<S extends Subject, N extends PersistentPropertyEntry<S>, O extends Property.Observer> extends Property<O> {
    
    /* -------------------------------------------------- Subject -------------------------------------------------- */
    
    /**
     * Returns the subject to which this property belongs.
     */
    @Pure
    public @Nonnull S getSubject();
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the property table that contains the property name, subject module and entry converter.
     */
    @Pure
    public @Nonnull PersistentPropertyTable<S, N> getTable();
    
    /* -------------------------------------------------- Reset -------------------------------------------------- */
    
    /**
     * Resets the values of this property so that they have to be reloaded from the database on the next retrieval.
     * If the state of the database changed in the meantime, then this method is impure.
     * However, read-only properties must be able to expose this method as well.
     * 
     * @throws ReentranceException if this method is called by an observer of this property.
     */
    @Pure
    @NonCommitting
    public void reset() throws DatabaseException, ReentranceException;
    
}
