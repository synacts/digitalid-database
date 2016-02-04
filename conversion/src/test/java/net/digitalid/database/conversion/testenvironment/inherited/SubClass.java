package net.digitalid.database.conversion.testenvironment.inherited;

import javax.annotation.Nonnull;

import net.digitalid.utility.conversion.annotations.Constructing;

/**
 *
 */
public class SubClass extends SuperClass {
    
    public final int number;
    
    private SubClass(int number, boolean flag) {
        super(flag);
        this.number = number;
    }
    
    @Constructing
    public static @Nonnull SubClass get(Integer number, Boolean flag) {
        return new SubClass(number, flag);
    }
    
}
