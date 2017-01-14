package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.identifier.column.SQLColumn;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * A boolean expression evaluates to a boolean.
 * 
 * @see SQLBinaryBooleanExpression
 * @see SQLBooleanLiteral
 * @see SQLExpressionInRangeBooleanExpression
 * @see SQLExpressionInSelectionBooleanExpression
 * @see SQLExpressionIsNotNullBooleanExpression
 * @see SQLExpressionIsNullBooleanExpression
 * @see SQLNumberComparisonBooleanExpression
 * @see SQLSelectionExistsBooleanExpression
 * @see SQLStringComparisonBooleanExpression
 * @see SQLUnaryBooleanExpression
 * 
 * @see SQLSelectStatement
 * @see SQLParameter
 * @see SQLColumn
 */
@Immutable
public interface SQLBooleanExpression extends SQLExpression {
    
    /* -------------------------------------------------- Unary Operations -------------------------------------------------- */
    
    /**
     * Returns the negated value of this boolean expression.
     */
    @Pure
    public default @Nonnull SQLUnaryBooleanExpression negated() {
        return SQLUnaryBooleanExpression.get(SQLUnaryBooleanOperator.NOT, this);
    }
    
    /* -------------------------------------------------- Binary Operations -------------------------------------------------- */
    
    /**
     * Returns the logical conjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression and(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.AND, this, expression);
    }
    
    /**
     * Returns the inclusive disjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression or(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.OR, this, expression);
    }
    
    /**
     * Returns the exclusive disjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression xor(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.XOR, this, expression);
    }
    
    /**
     * Returns the logical equality with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression equal(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.EQUAL, this, expression);
    }
    
    /**
     * Returns the logical inequality with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression unequal(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpression.get(SQLBinaryBooleanOperator.UNEQUAL, this, expression);
    }
    
}
