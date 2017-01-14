package net.digitalid.database.dialect.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLExpression;

/**
 * A string expression evaluates to a string.
 * 
 * @see SQLStringLiteral
 * @see SQLVariadicStringExpression
 * 
 * @see SQLSelectStatement
 * @see SQLParameter
 * @see SQLColumn
 */
@Immutable
public interface SQLStringExpression extends SQLExpression {
    
    /* -------------------------------------------------- Variadic Operations -------------------------------------------------- */
    
    /**
     * Returns the concatenation with the given string expression.
     */
    @Pure
    public default @Nonnull SQLVariadicStringExpression concat(@Nonnull SQLStringExpression expression) {
        return SQLVariadicStringExpression.get(SQLVariadicStringOperator.CONCAT, this, expression);
    }
    
}
