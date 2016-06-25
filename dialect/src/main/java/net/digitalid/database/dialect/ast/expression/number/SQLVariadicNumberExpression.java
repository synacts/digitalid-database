package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Captured;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collections.array.FreezableArray;
import net.digitalid.utility.collections.array.ReadOnlyArray;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.SQLVariadicExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

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
    private final @Frozen @NonNullableElements @Nonnull ReadOnlyArray<SQLNumberExpression> expressions;
    
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
        this.expressions = FreezableArray.withElements(expressions).freeze();
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
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLVariadicNumberExpression> transcriber = new Transcriber<SQLVariadicNumberExpression>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicNumberExpression node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.operator));
            string.append(node.expressions.map(expression -> dialect.transcribe(site, expression)).join(Brackets.ROUND));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLVariadicNumberExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public final void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        for (final @Nonnull SQLExpression expression : expressions) {
            expression.storeValues(collector);
        }
    }
    
}
