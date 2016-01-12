package net.digitalid.database.core.sql.expression.string;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

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
