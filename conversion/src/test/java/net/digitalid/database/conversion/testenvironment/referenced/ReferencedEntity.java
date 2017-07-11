package net.digitalid.database.conversion.testenvironment.referenced;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.generators.GenerateTableConverter;
import net.digitalid.utility.storage.enumerations.ForeignKeyAction;

@GenerateBuilder
@GenerateSubclass
@GenerateTableConverter(onDelete = ForeignKeyAction.CASCADE, onUpdate = ForeignKeyAction.RESTRICT)
public interface ReferencedEntity {
    
    @Pure
    public abstract int getId();
    
    @Pure
    public abstract int getOtherValue();
    
}
