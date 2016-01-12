package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.Captured;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.Frozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.readonly.ReadOnlyArray;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * This class implements a variadic number expression.
 */
@Immutable
public class SQLVariadicNumberExpression extends SQLNumberExpression implements SQLVariadicExpression<SQLVariadicNumberOperator, SQLNumberExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this variadic expression.
     */
    private final @Nonnull SQLVariadicNumberOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLVariadicNumberOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Stores the child expressions of this variadic expression.
     */
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<SQLNumberExpression> expressions;
    
    @Pure
    @Override
    public final @Nonnull @NonNullableElements @Frozen ReadOnlyArray<SQLNumberExpression> getExpressions() {
        return expressions;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new variadic number expression with the given parameters.
     * 
     * @param operator the number operator of the new variadic expression.
     * @param expressions the number expressions of the new variadic expression.
     */
    protected SQLVariadicNumberExpression(@Nonnull SQLVariadicNumberOperator operator, @Captured @Nonnull SQLNumberExpression... expressions) {
        this.operator = operator;
        this.expressions = FreezableArray.getNonNullable(expressions).freeze();
    }
    
    /**
     * Returns a new variadic number expression with the given parameters.
     * 
     * @param operator the number operator of the new variadic expression.
     * @param expressions the number expressions of the new variadic expression.
     * 
     * @return a new variadic number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLVariadicNumberExpression get(@Nonnull SQLVariadicNumberOperator operator, @Captured @Nonnull SQLNumberExpression... expressions) {
        return new SQLVariadicNumberExpression(operator, expressions);
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
