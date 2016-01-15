package net.digitalid.database.core.sql.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.freezable.Frozen;
import net.digitalid.utility.validation.reference.Captured;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.database.core.table.Site;

/**
 * This class implements a variadic string expression.
 */
@Immutable
public class SQLVariadicStringExpression extends SQLStringExpression implements SQLVariadicExpression<SQLVariadicStringOperator, SQLStringExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this variadic expression.
     */
    private final @Nonnull SQLVariadicStringOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLVariadicStringOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Stores the child expressions of this variadic expression.
     */
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<SQLStringExpression> expressions;
    
    @Pure
    @Override
    public final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<SQLStringExpression> getExpressions() {
        return expressions;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new variadic string expression with the given parameters.
     * 
     * @param operator the string operator of the new variadic expression.
     * @param expressions the string expressions of the new variadic expression.
     */
    protected SQLVariadicStringExpression(@Nonnull SQLVariadicStringOperator operator, @Captured @Nonnull SQLStringExpression... expressions) {
        this.operator = operator;
        this.expressions = FreezableArray.getNonNullable(expressions).freeze();
    }
    
    /**
     * Returns a new variadic string expression with the given parameters.
     * 
     * @param operator the string operator of the new variadic expression.
     * @param expressions the string expressions of the new variadic expression.
     * 
     * @return a new variadic string expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLVariadicStringExpression get(@Nonnull SQLVariadicStringOperator operator, @Captured @Nonnull SQLStringExpression... expressions) {
        return new SQLVariadicStringExpression(operator, expressions);
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
