package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLLiteral;
import net.digitalid.database.unit.Unit;

/**
 * A boolean literal.
 */
@Immutable
@GenerateSubclass
public interface SQLBooleanLiteral extends SQLBooleanExpression, SQLLiteral {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * Stores the SQL representation of 'true'.
     */
    public final @Nonnull SQLBooleanLiteral TRUE = new SQLBooleanLiteralSubclass(Boolean.TRUE);
    
    /**
     * Stores the SQL representation of 'false'.
     */
    public final @Nonnull SQLBooleanLiteral FALSE = new SQLBooleanLiteralSubclass(Boolean.FALSE);
    
    /**
     * Stores the SQL representation of 'null'.
     */
    public final @Nonnull SQLBooleanLiteral NULL = new SQLBooleanLiteralSubclass(null);
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value of this boolean literal.
     */
    @Pure
    public @Nullable Boolean getValue();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(String.valueOf(getValue()).toUpperCase());
    }
    
}
