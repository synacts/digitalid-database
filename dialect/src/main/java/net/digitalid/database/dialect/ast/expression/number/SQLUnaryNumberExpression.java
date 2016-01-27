package net.digitalid.database.dialect.ast.expression.number;

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
 * This class implements a unary number expression.
 */
@Immutable
public class SQLUnaryNumberExpression extends SQLNumberExpression implements SQLUnaryExpression<SQLUnaryNumberOperator, SQLNumberExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this unary expression.
     */
    private final @Nonnull SQLUnaryNumberOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLUnaryNumberOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Stores the child expression of this unary expression.
     */
    private final @Nonnull SQLNumberExpression expression;
    
    @Pure
    @Override
    public final @Nonnull SQLNumberExpression getExpression() {
        return expression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new unary number expression with the given parameters.
     * 
     * @param operator the operator of the new unary number expression.
     * @param expression the child expression of this unary number expression.
     */
    protected SQLUnaryNumberExpression(@Nonnull SQLUnaryNumberOperator operator, @Nonnull SQLNumberExpression expression) {
        this.operator = operator;
        this.expression = expression;
    }
    
    /**
     * Returns a new unary number expression with the given parameters.
     * 
     * @param operator the operator of the new unary number expression.
     * @param expression the child expression of this unary number expression.
     * 
     * @return a new unary number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLUnaryNumberExpression get(@Nonnull SQLUnaryNumberOperator operator, @Nonnull SQLNumberExpression expression) {
        return new SQLUnaryNumberExpression(operator, expression);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLUnaryNumberExpression> transcriber = new Transcriber<SQLUnaryNumberExpression>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryNumberExpression node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            dialect.transcribe(site, string, node.operator);
            string.append("(");
            dialect.transcribe(site, string, node.expression);
            string.append(")");
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLUnaryNumberExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        expression.storeValues(collector);
    }
    
}
