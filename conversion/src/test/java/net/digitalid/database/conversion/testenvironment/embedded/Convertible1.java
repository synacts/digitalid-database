package net.digitalid.database.conversion.testenvironment.embedded;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class Convertible1  {
    
    public final int value1;
    
    Convertible1(int value1) {
        this.value1 = value1;
    }
    
}
