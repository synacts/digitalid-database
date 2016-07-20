package net.digitalid.database.dialect.ast.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.dialect.ast.expression.bool.SQLUnaryBooleanExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLUnaryNumberExpression;

/**
 * All variadic expressions implement this interface.
 * 
 * @see SQLUnaryBooleanExpression
 * @see SQLUnaryNumberExpression
 */
public interface SQLUnaryExpression<O extends SQLUnaryOperator, E extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this unary expression.
     * 
     * @return the operator of this unary expression.
     */
    @Pure
    public abstract @Nonnull O getOperator();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the child expression of this unary expression.
     * 
     * @return the child expression of this unary expression.
     */
    @Pure
    public abstract @Nonnull E getExpression();
    
}
