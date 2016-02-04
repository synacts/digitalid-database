package net.digitalid.database.conversion.testenvironment.embedded;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.conversion.Convertible;

/**
 *
 */
public class Convertible1 implements Convertible {
    
    public final int value1;
    
    private Convertible1(int value1) {
        this.value1 = value1;
    }
    
    @Constructing
    public static @Nonnull Convertible1 get(int value1) {
        return new Convertible1(value1);
    }
    
}
