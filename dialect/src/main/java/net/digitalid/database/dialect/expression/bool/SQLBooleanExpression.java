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
        return SQLUnaryBooleanExpressionBuilder.withOperator(SQLUnaryBooleanOperator.NOT).withExpression(this).build();
    }
    
    /* -------------------------------------------------- Binary Operations -------------------------------------------------- */
    
    /**
     * Returns the logical conjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression and(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the inclusive disjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression or(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.OR).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the exclusive disjunction with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression xor(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.XOR).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the logical equality with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression equal(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the logical inequality with the given boolean expression.
     */
    @Pure
    public default @Nonnull SQLBinaryBooleanExpression unequal(@Nonnull SQLBooleanExpression expression) {
        return SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.UNEQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
}
