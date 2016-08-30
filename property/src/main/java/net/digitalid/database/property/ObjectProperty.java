package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.property.simple.SimpleObjectProperty;

/**
 * An object property belongs to an object and stores its value(s) in the database with the object used as the key.
 * 
 * @see SimpleObjectProperty
 */
@Mutable
public interface ObjectProperty<O, V> {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the property table that contains the property name, object and value converters, the required authorization and the value validator.
     */
    @Pure
    public @Nonnull PropertyTable<O, V> getTable();
    
    /* -------------------------------------------------- Object -------------------------------------------------- */
    
    /**
     * Returns the object to which this property belongs.
     */
    @Pure
    public @Nonnull O getObject();
    
}
