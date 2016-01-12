package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements an expression that evaluates to a number.
 */
@Immutable
public abstract class SQLNumberExpression implements SQLExpression {
    
    /* -------------------------------------------------- Unary Operations -------------------------------------------------- */
    
    /**
     * Returns the rounded value of this number expression.
     * 
     * @return the rounded value of this number expression.
     */
    @Pure
    public final @Nonnull SQLUnaryNumberExpression rounded() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.ROUND, this);
    }
    
    /**
     * Returns the negated value of this number expression.
     * 
     * @return the negated value of this number expression.
     */
    @Pure
    public final @Nonnull SQLUnaryNumberExpression negated() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.NEGATE, this);
    }
    
    /**
     * Returns the absolute value of this number expression.
     * 
     * @return the absolute value of this number expression.
     */
    @Pure
    public final @Nonnull SQLUnaryNumberExpression absolute() {
        return SQLUnaryNumberExpression.get(SQLUnaryNumberOperator.ABSOLUTE, this);
    }
    
    /* -------------------------------------------------- Binary Operations -------------------------------------------------- */
    
    /**
     * Returns the addition with the given number expression.
     * 
     * @param expression the number expression to be added.
     * 
     * @return the addition with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression add(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.ADDITION, this, expression);
    }
    
    /**
     * Returns the subtraction with the given number expression.
     * 
     * @param expression the number expression to be subtracted.
     * 
     * @return the subtraction with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression subtract(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.SUBTRACTION, this, expression);
    }
    
    /**
     * Returns the multiplication with the given number expression.
     * 
     * @param expression the number expression to be multiplied with.
     * 
     * @return the multiplication with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression multiply(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MULTIPLICATION, this, expression);
    }
    
    /**
     * Returns the division with the given number expression.
     * 
     * @param expression the number expression to be divided by.
     * 
     * @return the division with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression divide(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.DIVISION, this, expression);
    }
    
    /**
     * Returns the integer division with the given number expression.
     * 
     * @param expression the number expression to be divided by.
     * 
     * @return the integer division with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression divideInteger(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.INTEGER_DIVISION, this, expression);
    }
    
    /**
     * Returns the modulo with the given number expression.
     * 
     * @param expression the number expression to be divided by.
     * 
     * @return the modulo with the given number expression.
     */
    @Pure
    public final @Nonnull SQLBinaryNumberExpression modulo(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpression.get(SQLBinaryNumberOperator.MODULO, this, expression);
    }
    
    /* -------------------------------------------------- Comparison Operations -------------------------------------------------- */
    
    /**
     * Returns the inclusive disjunction with the given boolean expression.
     * 
     * @param expression the boolean expression to combine with this one.
     * 
     * @return the inclusive disjunction with the given boolean expression.
     */
//    @Pure
//    public final @Nonnull SQLNumberComparisonBooleanExpression equal(@Nonnull SQLNumberExpression expression) {
//        return SQLNumberComparisonBooleanExpression.get(SQLBinaryBooleanOperator.OR, this, expression);
//    }
    
}
