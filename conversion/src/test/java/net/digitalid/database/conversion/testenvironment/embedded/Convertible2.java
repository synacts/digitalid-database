package net.digitalid.database.conversion.testenvironment.embedded;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Convertible2  {
    
    public final int value2;
    
    Convertible2(int value2) {
        this.value2 = value2;
    }
    
}
