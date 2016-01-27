package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryExpression;
import net.digitalid.database.dialect.ast.utility.binary.SQLBinaryExpressionTranscriber;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;

/**
 * This class implements a binary number expression.
 */
@Immutable
public class SQLBinaryNumberExpression extends SQLNumberExpression implements SQLBinaryExpression<SQLBinaryNumberOperator, SQLNumberExpression> {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this binary expression.
     */
    private final @Nonnull SQLBinaryNumberOperator operator;
    
    @Pure
    @Override
    public final @Nonnull SQLBinaryNumberOperator getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Left Expression -------------------------------------------------- */
    
    /**
     * Stores the left child expression of this binary expression.
     */
    private final @Nonnull SQLNumberExpression leftExpression;
    
    @Pure
    @Override
    public final @Nonnull SQLNumberExpression getLeftExpression() {
        return leftExpression;
    }
    
    /* -------------------------------------------------- Right Expression -------------------------------------------------- */
    
    /**
     * Stores the right child expression of this binary expression.
     */
    private final @Nonnull SQLNumberExpression rightExpression;
    
    @Pure
    @Override
    public final @Nonnull SQLNumberExpression getRightExpression() {
        return rightExpression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new binary number expression with the given parameters.
     * 
     * @param operator the operator of the new binary number expression.
     * @param leftExpression the left child expression of the new binary number expression.
     * @param rightExpression the right child expression of the new binary number expression.
     */
    protected SQLBinaryNumberExpression(@Nonnull SQLBinaryNumberOperator operator, @Nonnull SQLNumberExpression leftExpression, @Nonnull SQLNumberExpression rightExpression) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    
    /**
     * Returns a new binary number expression with the given parameters.
     * 
     * @param operator the operator of the new binary number expression.
     * @param leftExpression the left child expression of the new binary number expression.
     * @param rightExpression the right child expression of the new binary number expression.
     * 
     * @return a new binary number expression with the given parameters.
     */
    @Pure
    public static @Nonnull SQLBinaryNumberExpression get(@Nonnull SQLBinaryNumberOperator operator, @Nonnull SQLNumberExpression leftExpression, @Nonnull SQLNumberExpression rightExpression) {
        return new SQLBinaryNumberExpression(operator, leftExpression, rightExpression);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLBinaryNumberExpression> transcriber = new Transcriber<SQLBinaryNumberExpression>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBinaryNumberExpression node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            SQLBinaryExpressionTranscriber.transcribeNode(node, dialect, site, string);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLBinaryNumberExpression> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        leftExpression.storeValues(collector);
        rightExpression.storeValues(collector);
    }
    
}
