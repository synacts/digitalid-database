package net.digitalid.database.conversion.testenvironment.embedded;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.conversion.Convertible;

/**
 *
 */
public class Convertible2 implements Convertible {
    
    public final int value2;
    
    private Convertible2(int value2) {
        this.value2 = value2;
    }
    
    @Constructing
    public static @Nonnull Convertible2 get(int value2) {
        return new Convertible2(value2);
    }
    
}
