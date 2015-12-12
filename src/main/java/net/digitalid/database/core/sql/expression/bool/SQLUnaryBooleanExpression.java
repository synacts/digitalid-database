package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.SQLUnaryExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.internal.InternalException;

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
