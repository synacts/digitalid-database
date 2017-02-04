package net.digitalid.database.testing.assertion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public interface ExpectedColumnDeclaration {
    
    @Impure
    abstract void setFound(boolean found);

    @Pure
    abstract boolean isFound();

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
    
    @Pure
    @Default("null")
    public abstract @Nullable String getColumnConstraint();
}
