package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLBinaryExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class implements a binary number expression.
 */
@Immutable
public class SQLBinaryNumberExpression extends SQLNumberExpression implements SQLBinaryExpression<SQLBinaryNumberOperator, SQLNumberExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this binary expression.
     */
    private final @Nonnull SQLBinaryNumberOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLBinaryNumberOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Left Expression -------------------------------------------------- */
    
    /**
     * Stores the left child expression of this binary expression.
     */
    private final @Nonnull SQLNumberExpression leftExpression;
    
    @Pure
    @Override
    public final @Nonnull SQLNumberExpression getLeftExpression() {
        return leftExpression;
    }
    
    /* -------------------------------------------------- Right Expression -------------------------------------------------- */
    
    /**
     * Stores the right child expression of this binary expression.
     */
    private final @Nonnull SQLNumberExpression rightExpression;
    
    @Pure
    @Override
    public final @Nonnull SQLNumberExpression getRightExpression() {
        return rightExpression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new binary number expression with the given parameters.
     * 
     * @param operator the operator of the new binary number expression.
     * @param leftExpression the left child expression of the new binary number expression.
     * @param rightExpression the right child expression of the new binary number expression.
     */
    protected SQLBinaryNumberExpression(@Nonnull SQLBinaryNumberOperator operator, @Nonnull SQLNumberExpression leftExpression, @Nonnull SQLNumberExpression rightExpression) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
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
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        leftExpression.storeValues(collector);
        rightExpression.storeValues(collector);
    }
    
}
