package net.digitalid.database.conversion.testenvironment.simple;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.conversion.Convertible;

/**
 *
 */
public class MultiBooleanColumnTable implements Convertible {
    
    public final @Nonnull Boolean firstValue;
    public final @Nonnull Boolean secondValue;
    
    protected MultiBooleanColumnTable(@Nonnull Boolean firstValue, @Nonnull Boolean secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }
    
    @Constructing
    public static @Nonnull MultiBooleanColumnTable get(@Nonnull Boolean firstValue, @Nonnull Boolean secondValue) {
        return new MultiBooleanColumnTable(firstValue, secondValue);
    }
    
}
