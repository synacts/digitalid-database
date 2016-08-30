package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.simple.PersistentWritableSimpleProperty;

/**
 * A persistent property has a time of last modification and can be reset.
 * 
 * @see PersistentWritableSimpleProperty
 */
@Mutable
public interface PersistentProperty {
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    /**
     * Returns the time of the last modification.
     */
    @Pure
    @NonCommitting
    @TODO(task = "It might make sense to have a time of last modification only for simple properties.", date = "2016-08-30", author = Author.KASPAR_ETTER)
    public @Nonnull Time getTime() throws DatabaseException;
    
    /* -------------------------------------------------- Reset -------------------------------------------------- */
    
    /**
     * Resets the time and value of this property.
     */
    @Impure
    @NonCommitting
    public void reset() throws DatabaseException;
    
}
