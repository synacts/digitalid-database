package net.digitalid.database.conversion.testenvironment.referenced;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.converter.Convertible;

/**
 *
 */
public class ReferencedEntity implements Convertible {
    
    public final int id;
    
    public final int otherValue;
    
    private ReferencedEntity(int id, int otherValue) {
        this.id = id;
        this.otherValue = otherValue;
    }
    
    public static @Nonnull ReferencedEntity get(int id, int otherValue) {
        return new ReferencedEntity(id, otherValue);
    }
    
}
