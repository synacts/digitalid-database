package net.digitalid.database.testing.assertion;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public abstract class ExpectedColumnDeclaration {
    
    boolean found = false;

    @Pure
    public abstract @Nonnull String getColumnName();

    @Pure
    public abstract @Nonnull String getDBType();

    @Pure
    @Default("true")
    public abstract boolean isNullAllowed();

    @Pure
    @Default("\"\"")
    public abstract @Nonnull String getKey();

    @Pure
    @Default("\"NULL\"")
    public abstract @Nonnull String getDefaultValue();
    
}
