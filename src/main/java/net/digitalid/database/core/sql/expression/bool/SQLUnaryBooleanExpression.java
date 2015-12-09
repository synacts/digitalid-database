package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLUnaryExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a unary boolean expression.
 */
@Immutable
public class SQLUnaryBooleanExpression extends SQLUnaryExpression<SQLUnaryBooleanOperator, SQLBooleanExpression> implements SQLBooleanExpression {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new unary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new unary boolean expression.
     * @param expression the child expression of this unary boolean expression.
     */
    protected SQLUnaryBooleanExpression(@Nonnull SQLUnaryBooleanOperator operator, @Nonnull SQLBooleanExpression expression) {
        super(operator, expression);
    }
    
    /**
     * Returns a new unary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new unary boolean expression.
     * @param expression the child expression of this unary boolean expression.
     * 
     * @return a new unary boolean expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLUnaryBooleanExpression get(@Nonnull SQLUnaryBooleanOperator operator, @Nonnull SQLBooleanExpression expression) {
        return new SQLUnaryBooleanExpression(operator, expression);
    }
    
}
