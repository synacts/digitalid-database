package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLBinaryExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a binary boolean expression.
 */
@Immutable
public class SQLBinaryBooleanExpression extends SQLBinaryExpression<SQLBinaryBooleanOperator, SQLBooleanExpression> implements SQLBooleanExpression {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new binary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new binary boolean expression.
     * @param leftExpression the left child expression of the new binary boolean expression.
     * @param rightExpression the right child expression of the new binary boolean expression.
     */
    protected SQLBinaryBooleanExpression(@Nonnull SQLBinaryBooleanOperator operator, @Nonnull SQLBooleanExpression leftExpression, @Nonnull SQLBooleanExpression rightExpression) {
        super(operator, leftExpression, rightExpression);
    }
    
    /**
     * Returns a new binary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new binary boolean expression.
     * @param leftExpression the left child expression of the new binary boolean expression.
     * @param rightExpression the right child expression of the new binary boolean expression.
     * 
     * @return a new binary boolean expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLBinaryBooleanExpression get(@Nonnull SQLBinaryBooleanOperator operator, @Nonnull SQLBooleanExpression leftExpression, @Nonnull SQLBooleanExpression rightExpression) {
        return new SQLBinaryBooleanExpression(operator, leftExpression, rightExpression);
    }
    
}
