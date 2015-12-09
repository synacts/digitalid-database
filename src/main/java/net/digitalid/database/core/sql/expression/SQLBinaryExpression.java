package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.bool.SQLBinaryBooleanExpression;
import net.digitalid.database.core.sql.expression.number.SQLBinaryNumberExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class implements a binary expression.
 * 
 * @see SQLBinaryBooleanExpression
 * @see SQLBinaryNumberExpression
 */
@Immutable
public abstract class SQLBinaryExpression<O extends SQLBinaryOperator, E extends SQLExpression> implements SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this binary expression.
     */
    private final @Nonnull O operator;
    
    /**
     * Returns the operator of this binary expression.
     * 
     * @return the operator of this binary expression.
     */
    @Pure
    public final @Nonnull O getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Left Expression -------------------------------------------------- */
    
    /**
     * Stores the left child expression of this binary expression.
     */
    private final @Nonnull E leftExpression;
    
    /**
     * Returns the left child expression of this binary expression.
     * 
     * @return the left child expression of this binary expression.
     */
    @Pure
    public final @Nonnull E getLeftExpression() {
        return leftExpression;
    }
    
    /* -------------------------------------------------- Right Expression -------------------------------------------------- */
    
    /**
     * Stores the right child expression of this binary expression.
     */
    private final @Nonnull E rightExpression;
    
    /**
     * Returns the right child expression of this binary expression.
     * 
     * @return the right child expression of this binary expression.
     */
    @Pure
    public final @Nonnull E getRightExpression() {
        return rightExpression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new binary expression with the given parameters.
     * 
     * @param operator the operator of the new binary expression.
     * @param leftExpression the left child expression of the new binary expression.
     * @param rightExpression the right child expression of the new binary expression.
     */
    protected SQLBinaryExpression(@Nonnull O operator, @Nonnull E leftExpression, @Nonnull E rightExpression) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
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
