package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.bool.SQLUnaryBooleanExpression;
import net.digitalid.database.core.sql.expression.number.SQLUnaryNumberExpression;
import net.digitalid.utility.validation.state.Pure;

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
    public @Nonnull O getOperator();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the child expression of this unary expression.
     * 
     * @return the child expression of this unary expression.
     */
    @Pure
    public @Nonnull E getExpression();
    
}
