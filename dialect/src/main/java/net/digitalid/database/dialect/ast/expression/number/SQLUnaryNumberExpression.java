package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLUnaryExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.interfaces.Site;

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
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryNumberExpression node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.operator));
            string.append(Brackets.inRound(dialect.transcribe(site, node.expression)));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLUnaryNumberExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Pure
    @Override
    public final void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        expression.storeValues(collector);
    }
    
}
