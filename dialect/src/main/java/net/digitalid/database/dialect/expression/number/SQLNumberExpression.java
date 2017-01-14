package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLExpression;

/**
 * A number expression evaluates to a number.
 * 
 * @see SQLBinaryNumberExpression
 * @see SQLNumberLiteral
 * @see SQLRowCountNumberExpression
 * @see SQLUnaryNumberExpression
 * @see SQLVariadicNumberExpression
 * 
 * @see SQLSelectStatement
 * @see SQLParameter
 * @see SQLColumn
 */
@Immutable
public interface SQLNumberExpression extends SQLExpression {
    
    /* -------------------------------------------------- Unary Operations -------------------------------------------------- */
    
    /**
     * Returns the rounded value of this number expression.
     */
    @Pure
    public default @Nonnull SQLUnaryNumberExpression rounded() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.ROUND, this);
    }
    
    /**
     * Returns the negative value of this number expression.
     */
    @Pure
    public default @Nonnull SQLUnaryNumberExpression negative() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.NEGATIVE, this);
    }
    
    /**
     * Returns the absolute value of this number expression.
     */
    @Pure
    public default @Nonnull SQLUnaryNumberExpression absolute() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.ABSOLUTE, this);
    }
    
    /* -------------------------------------------------- Binary Operations -------------------------------------------------- */
    
    /**
     * Returns the addition with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression add(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.ADDITION, this, expression);
    }
    
    /**
     * Returns the subtraction with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression subtract(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.SUBTRACTION, this, expression);
    }
    
    /**
     * Returns the multiplication with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression multiply(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MULTIPLICATION, this, expression);
    }
    
    /**
     * Returns the division with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression divide(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.DIVISION, this, expression);
    }
    
    /**
     * Returns the integer division with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression divideInteger(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.INTEGER_DIVISION, this, expression);
    }
    
    /**
     * Returns the modulo with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression modulo(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MODULO, this, expression);
    }
    
    /* -------------------------------------------------- Comparison Operations -------------------------------------------------- */
    
    /**
     * Returns the inclusive disjunction with the given boolean expression.
     */
//    @Pure
//    public default @Nonnull SQLNumberComparisonBooleanExpression equal(@Nonnull SQLNumberExpression expression) {
//        return SQLNumberComparisonBooleanExpression.get(SQLBinaryBooleanOperator.OR, this, expression);
//    }
    
}
