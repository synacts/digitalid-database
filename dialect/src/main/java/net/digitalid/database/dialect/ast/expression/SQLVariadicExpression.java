package net.digitalid.database.dialect.ast.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.dialect.ast.expression.number.SQLVariadicNumberExpression;
import net.digitalid.database.dialect.ast.expression.string.SQLVariadicStringExpression;

/**
 * All variadic expressions implement this interface.
 * 
 * @see SQLVariadicNumberExpression
 * @see SQLVariadicStringExpression
 */
public interface SQLVariadicExpression<O extends SQLVariadicOperator, E extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this variadic expression.
     * 
     * @return the operator of this variadic expression.
     */
    @Pure
    public abstract @Nonnull O getOperator();
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Returns the child expressions of this variadic expression.
     * 
     * @return the child expressions of this variadic expression.
     */
    @Pure
    public abstract @Nonnull @NonNullableElements @Frozen ReadOnlyArray<E> getExpressions();
    
}
