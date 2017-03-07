package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.unit.Unit;

/**
 * A null literal.
 */
@Immutable
@GenerateSubclass
public interface SQLNullLiteral extends SQLBooleanExpression, SQLNumberExpression, SQLStringExpression, SQLLiteral {
    
    /* -------------------------------------------------- Instances -------------------------------------------------- */
    
    /**
     * Stores an instance of the surrounding class.
     */
    public static final @Nonnull SQLNullLiteral INSTANCE = new SQLNullLiteralSubclass();
    
    /**
     * Stores an instance of the surrounding class as a boolean expression.
     */
    public static final @Nonnull SQLBooleanExpression BOOLEAN = INSTANCE;
    
    /**
     * Stores an instance of the surrounding class as a number expression.
     */
    public static final @Nonnull SQLNumberExpression NUMBER = INSTANCE;
    
    /**
     * Stores an instance of the surrounding class as a string expression.
     */
    public static final @Nonnull SQLStringExpression STRING = INSTANCE;
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("NULL");
    }
    
}
