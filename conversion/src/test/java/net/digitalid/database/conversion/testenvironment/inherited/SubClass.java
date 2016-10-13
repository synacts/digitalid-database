package net.digitalid.database.conversion.testenvironment.inherited;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;

/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class SubClass extends SuperClass {
    
    public final int number;
    
    SubClass(int number, boolean flag) {
        super(flag);
        this.number = number;
    }
    
}
