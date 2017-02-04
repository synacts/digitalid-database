package net.digitalid.database.testing.assertion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.enumerations.ForeignKeyAction;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public interface ExpectedForeignKeyConstraint {
   
    @Pure
    public @Nonnull String getForeignKeyColumn();
    
    @Pure
    public @Nonnull String getReferencedTable();
    
    @Pure
    public @Nonnull String getReferencedColumn();
    
    @Pure
    public @Nullable ForeignKeyAction getDeleteAction();
    
    @Pure
    public @Nullable ForeignKeyAction getUpdateAction();
    
}
