package net.digitalid.database.conversion.testenvironment.referenced;

import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class ReferencedEntity  {
    
    public final int id;
    
    public final int otherValue;
    
    ReferencedEntity(int id, int otherValue) {
        this.id = id;
        this.otherValue = otherValue;
    }
    
}
