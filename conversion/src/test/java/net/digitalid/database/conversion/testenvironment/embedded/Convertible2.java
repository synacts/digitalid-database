package net.digitalid.database.conversion.testenvironment.embedded;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Convertible2  {
    
    public final int value;
    
    Convertible2(int value) {
        this.value = value;
    }
    
}
