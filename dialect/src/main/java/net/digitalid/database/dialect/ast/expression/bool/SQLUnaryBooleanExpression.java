package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLUnaryExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This class implements a unary boolean expression.
 */
@Immutable
public class SQLUnaryBooleanExpression extends SQLBooleanExpression implements SQLUnaryExpression<SQLUnaryBooleanOperator, SQLBooleanExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this unary expression.
     */
    private final @Nonnull SQLUnaryBooleanOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLUnaryBooleanOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Stores the child expression of this unary expression.
     */
    private final @Nonnull SQLBooleanExpression expression;
    
    @Pure
    @Override
    public final @Nonnull SQLBooleanExpression getExpression() {
        return expression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new unary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new unary boolean expression.
     * @param expression the child expression of this unary boolean expression.
     */
    protected SQLUnaryBooleanExpression(@Nonnull SQLUnaryBooleanOperator operator, @Nonnull SQLBooleanExpression expression) {
        this.operator = operator;
        this.expression = expression;
    }
    
    /**
     * Returns a new unary boolean expression with the given parameters.
     * 
     * @param operator the operator of the new unary boolean expression.
     * @param expression the child expression of this unary boolean expression.
     * 
     * @return a new unary boolean expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLUnaryBooleanExpression get(@Nonnull SQLUnaryBooleanOperator operator, @Nonnull SQLBooleanExpression expression) {
        return new SQLUnaryBooleanExpression(operator, expression);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLUnaryBooleanExpression> transcriber = new Transcriber<SQLUnaryBooleanExpression>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryBooleanExpression node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.operator));
            string.append(Brackets.inRound(dialect.transcribe(site, node.expression)));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLUnaryBooleanExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public final void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        expression.storeValues(collector);
    }
    
}
