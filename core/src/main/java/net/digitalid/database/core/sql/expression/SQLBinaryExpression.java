package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.sql.expression.bool.SQLBinaryBooleanExpression;
import net.digitalid.database.core.sql.expression.number.SQLBinaryNumberExpression;

/**
 * All binary expressions implement this interface.
 * 
 * @see SQLBinaryBooleanExpression
 * @see SQLBinaryNumberExpression
 */
public interface SQLBinaryExpression<O extends SQLBinaryOperator, E extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this binary expression.
     * 
     * @return the operator of this binary expression.
     */
    @Pure
    public @Nonnull O getOperator();
    
    /* -------------------------------------------------- Left Expression -------------------------------------------------- */
    
    /**
     * Returns the left child expression of this binary expression.
     * 
     * @return the left child expression of this binary expression.
     */
    @Pure
    public @Nonnull E getLeftExpression();
    
    /* -------------------------------------------------- Right Expression -------------------------------------------------- */
    
    /**
     * Returns the right child expression of this binary expression.
     * 
     * @return the right child expression of this binary expression.
     */
    @Pure
    public @Nonnull E getRightExpression();
    
}
