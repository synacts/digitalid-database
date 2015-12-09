package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.number.SQLVariadicNumberExpression;
import net.digitalid.database.core.sql.expression.string.SQLVariadicStringExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.Captured;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.Frozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class implements a variadic expression.
 * 
 * @see SQLVariadicNumberExpression
 * @see SQLVariadicStringExpression
 */
@Immutable
public abstract class SQLVariadicExpression<O extends SQLVariadicOperator, E extends SQLExpression> implements SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this variadic expression.
     */
    private final @Nonnull O operator;
    
    /**
     * Returns the operator of this variadic expression.
     * 
     * @return the operator of this variadic expression.
     */
    @Pure
    public final @Nonnull O getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Stores the expressions of this variadic expression.
     */
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<E> expressions;
    
    /**
     * Returns the expressions of this variadic expression.
     * 
     * @return the expressions of this variadic expression.
     */
    @Pure
    public final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<E> getExpressions() {
        return expressions;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new variadic expression with the given parameters.
     * 
     * @param operator the operator of the new variadic expression.
     * @param expressions the expressions of the new variadic expression.
     */
    @SafeVarargs
    protected SQLVariadicExpression(@Nonnull O operator, @Captured @Nonnull E... expressions) {
        this.operator = operator;
        this.expressions = FreezableArray.getNonNullable(expressions).freeze();
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        for (final @Nonnull SQLExpression expression : expressions) {
            expression.storeValues(collector);
        }
    }
    
}
