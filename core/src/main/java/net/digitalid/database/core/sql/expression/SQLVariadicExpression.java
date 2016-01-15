package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.freezable.Frozen;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.sql.expression.number.SQLVariadicNumberExpression;
import net.digitalid.database.core.sql.expression.string.SQLVariadicStringExpression;

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
    public @Nonnull O getOperator();
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Returns the child expressions of this variadic expression.
     * 
     * @return the child expressions of this variadic expression.
     */
    @Pure
    public @Nonnull @NonNullableElements @Frozen ReadOnlyArray<E> getExpressions();
    
}
