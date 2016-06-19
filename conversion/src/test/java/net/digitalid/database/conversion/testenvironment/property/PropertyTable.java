package net.digitalid.database.conversion.testenvironment.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.converter.Convertible;

/**
 *
 */
public class PropertyTable implements Convertible {
    
    public final @Nonnull TestProperty myProperty = TestProperty.getProperty();
    
    private PropertyTable() {}
    
    @Constructing
    public static @Nonnull PropertyTable get() {
        return new PropertyTable();
    }
    
}
