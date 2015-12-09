package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLBinaryExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements a binary number expression.
 */
@Immutable
public class SQLBinaryNumberExpression extends SQLBinaryExpression<SQLBinaryNumberOperator, SQLNumberExpression> implements SQLNumberExpression {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new binary number expression with the given parameters.
     * 
     * @param operator the operator of the new binary number expression.
     * @param leftExpression the left child expression of the new binary number expression.
     * @param rightExpression the right child expression of the new binary number expression.
     */
    protected SQLBinaryNumberExpression(@Nonnull SQLBinaryNumberOperator operator, @Nonnull SQLNumberExpression leftExpression, @Nonnull SQLNumberExpression rightExpression) {
        super(operator, leftExpression, rightExpression);
    }
    
    /**
     * Returns a new binary number expression with the given parameters.
     * 
     * @param operator the operator of the new binary number expression.
     * @param leftExpression the left child expression of the new binary number expression.
     * @param rightExpression the right child expression of the new binary number expression.
     * 
     * @return a new binary number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLBinaryNumberExpression get(@Nonnull SQLBinaryNumberOperator operator, @Nonnull SQLNumberExpression leftExpression, @Nonnull SQLNumberExpression rightExpression) {
        return new SQLBinaryNumberExpression(operator, leftExpression, rightExpression);
    }
    
}
