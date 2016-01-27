package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.dialect.ast.expression.SQLUnaryExpression;
import net.digitalid.database.core.table.Site;

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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryBooleanExpression node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            dialect.transcribe(site, string, node.operator);
            string.append("(");
            dialect.transcribe(site, string, node.expression);
            string.append(")");
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLUnaryBooleanExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        expression.storeValues(collector);
    }
    
}
