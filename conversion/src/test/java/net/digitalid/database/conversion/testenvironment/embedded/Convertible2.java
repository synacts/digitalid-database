package net.digitalid.database.conversion.testenvironment.embedded;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;


/**
 *
 */
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
public interface Convertible2  {
    
    @Pure
    public int getValue();
    
}
