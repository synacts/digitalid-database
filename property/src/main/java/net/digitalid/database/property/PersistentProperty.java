package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * A persistent property stores its value(s) in the database.
 */
@Mutable
public interface PersistentProperty<O, V> {
    
    /**
     * Returns the property table that contains the property name, object and value converters, the required authorization and the value validator.
     */
    @Pure
    public @Nonnull PropertyTable<O, V> getTable();
    
    /**
     * Returns the object to which this property belongs.
     */
    @Pure
    public @Nonnull O getObject();
    
    /**
     * Returns the time of the last modification.
     */
    @Pure
    @NonCommitting
    public @Nonnull Time getTime() throws DatabaseException;
    
    /**
     * Resets the time and value of this property.
     */
    @Impure
    @NonCommitting
    public void reset() throws DatabaseException;
    
}
