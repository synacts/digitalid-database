package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.metadata.PrimaryKey;
import net.digitalid.database.property.simple.SimplePropertyEntry;

/**
 * This class models an entry in the {@link PropertyTable}.
 * 
 * @see SimplePropertyEntry
 */
@Immutable
public interface PropertyEntry<O, V> {
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    /**
     * Returns the object to which the property belongs.
     */
    @Pure
    @PrimaryKey
    public @Nonnull O getObject();
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    /**
     * Returns the time of the last modification.
     */
    @Pure
    public @Nonnull Time getTime();
    
}
