package net.digitalid.database.testing.assertion;

import java.util.Set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public interface ExpectedTableConstraint {
    
    @Pure
    public @Nonnull String getTableName();
    
    @Pure
    public @Nonnull String getSchema();
    
    @Pure
    public @Nonnull Set<@Nonnull ExpectedForeignKeyConstraint> getForeignKeyConstraints();
    
}
