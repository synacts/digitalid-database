package net.digitalid.database.conversion.testenvironment.referenced;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.enumerations.ForeignKeyAction;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.unit.Unit;


/**
 *
 */
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
@ForeignKey(onDelete = ForeignKeyAction.CASCADE)
public interface ReferencedEntity extends Subject<Unit> {
    
    @Pure
    public abstract int getId();
    
    @Pure
    public abstract int getOtherValue();
    
}
