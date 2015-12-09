package net.digitalid.database.core.sql.expression;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.expression.bool.SQLUnaryBooleanExpression;
import net.digitalid.database.core.sql.expression.number.SQLUnaryNumberExpression;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class implements a unary expression.
 * 
 * @see SQLUnaryBooleanExpression
 * @see SQLUnaryNumberExpression
 */
@Immutable
public abstract class SQLUnaryExpression<O extends SQLUnaryOperator, E extends SQLExpression> implements SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Stores the operator of this unary expression.
     */
    private final @Nonnull O operator;
    
    /**
     * Returns the operator of this unary expression.
     * 
     * @return the operator of this unary expression.
     */
    @Pure
    public final @Nonnull O getOperator() {
        return operator;
    }
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Stores the child expression of this unary expression.
     */
    private final @Nonnull E expression;
    
    /**
     * Returns the child expression of this unary expression.
     * 
     * @return the child expression of this unary expression.
     */
    @Pure
    public final @Nonnull E getExpression() {
        return expression;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new unary expression with the given parameters.
     * 
     * @param operator the operator of the new unary expression.
     * @param expression the child expression of this unary expression.
     */
    protected SQLUnaryExpression(@Nonnull O operator, @Nonnull E expression) {
        this.operator = operator;
        this.expression = expression;
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
