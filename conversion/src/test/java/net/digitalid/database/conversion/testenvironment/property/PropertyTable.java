package net.digitalid.database.conversion.testenvironment.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.annotations.Constructing;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;


/**
 *
 */
@GenerateBuilder
@GenerateConverter
public class PropertyTable  {
    
    public final @Nonnull TestProperty myProperty = TestPropertyBuilder.get().build();
    
    PropertyTable() {}
    
}
