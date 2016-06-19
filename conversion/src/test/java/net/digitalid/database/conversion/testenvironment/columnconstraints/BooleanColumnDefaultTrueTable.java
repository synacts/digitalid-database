package net.digitalid.database.conversion.testenvironment.columnconstraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.conversion.converter.Convertible;

import net.digitalid.database.dialect.annotations.Default;

/**
 *
 */
public class BooleanColumnDefaultTrueTable implements Convertible {
    
    @Default("TRUE")
    public final @Nonnull Boolean value;
    
    protected BooleanColumnDefaultTrueTable(@Nonnull Boolean value) {
        this.value = value;
    }
    
    @Constructing
    public static @Nonnull BooleanColumnDefaultTrueTable get(@Nonnull Boolean value) {
        return new BooleanColumnDefaultTrueTable(value);
    }
    
}
