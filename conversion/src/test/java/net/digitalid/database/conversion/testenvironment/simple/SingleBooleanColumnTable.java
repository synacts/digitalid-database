package net.digitalid.database.conversion.testenvironment.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.converter.Convertible;

/**
 *
 */
public class SingleBooleanColumnTable implements Convertible {
    
    public final @Nonnull Boolean value;
    
    protected SingleBooleanColumnTable(@Nonnull Boolean value) {
        this.value = value;
    }
    
    @Constructing
    public static @Nonnull SingleBooleanColumnTable get(@Nonnull Boolean value) {
        return new SingleBooleanColumnTable(value);
    }
    
}
