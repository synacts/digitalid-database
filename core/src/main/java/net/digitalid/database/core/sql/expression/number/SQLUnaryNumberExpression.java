package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLUnaryExpression;
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
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public final void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        expression.storeValues(collector);
    }
    
}
