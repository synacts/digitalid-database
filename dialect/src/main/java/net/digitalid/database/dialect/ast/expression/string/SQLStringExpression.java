package net.digitalid.database.dialect.ast.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.method.Pure;

import net.digitalid.database.dialect.ast.expression.SQLExpression;

/**
 * This class implements an expression that evaluates to a string.
 */
@Immutable
public abstract class SQLStringExpression implements SQLExpression {
    
    /* -------------------------------------------------- Variadic Operations -------------------------------------------------- */
    
    /**
     * Returns the concatenation with the given string expression.
     * 
     * @param expression the string expression to be concatenated.
     * 
     * @return the concatenation with the given string expression.
     */
    @Pure
    public final @Nonnull SQLVariadicStringExpression concat(@Nonnull SQLStringExpression expression) {
        return SQLVariadicStringExpression.get(SQLVariadicStringOperator.CONCAT, this, expression);
    }
    
}
