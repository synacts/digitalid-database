package net.digitalid.database.dialect.ast.expression.string;

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

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.SQLVariadicExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.interfaces.Site;

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
    private final @Frozen @NonNullableElements @Nonnull ReadOnlyArray<SQLStringExpression> expressions;
    
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
        this.expressions = FreezableArray.withElements(expressions).freeze();
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
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLVariadicStringExpression> transcriber = new Transcriber<SQLVariadicStringExpression>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicStringExpression node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.operator));
            string.append(node.expressions.map(expression -> dialect.transcribe(site, expression)).join(Brackets.ROUND));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLVariadicStringExpression> getTranscriber() {
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
