package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLUnaryExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a unary number expression.
 */
@Immutable
public class SQLUnaryNumberExpression extends SQLUnaryExpression<SQLUnaryNumberOperator, SQLNumberExpression> implements SQLNumberExpression {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new unary number expression with the given parameters.
     * 
     * @param operator the operator of the new unary number expression.
     * @param expression the child expression of this unary number expression.
     */
    protected SQLUnaryNumberExpression(@Nonnull SQLUnaryNumberOperator operator, @Nonnull SQLNumberExpression expression) {
        super(operator, expression);
    }
    
    /**
     * Returns a new unary number expression with the given parameters.
     * 
     * @param operator the operator of the new unary number expression.
     * @param expression the child expression of this unary number expression.
     * 
     * @return a new unary number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLUnaryNumberExpression get(@Nonnull SQLUnaryNumberOperator operator, @Nonnull SQLNumberExpression expression) {
        return new SQLUnaryNumberExpression(operator, expression);
    }
    
}
