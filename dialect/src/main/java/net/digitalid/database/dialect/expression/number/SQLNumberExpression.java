/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLNumberInRangeBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLNumberInRangeBooleanExpressionBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumn;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

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
        return SQLUnaryNumberExpressionBuilder.withOperator(SQLUnaryNumberOperator.ROUND).withExpression(this).build();
    }
    
    /**
     * Returns the negative value of this number expression.
     */
    @Pure
    public default @Nonnull SQLUnaryNumberExpression negative() {
        return SQLUnaryNumberExpressionBuilder.withOperator(SQLUnaryNumberOperator.NEGATIVE).withExpression(this).build();
    }
    
    /**
     * Returns the absolute value of this number expression.
     */
    @Pure
    public default @Nonnull SQLUnaryNumberExpression absolute() {
        return SQLUnaryNumberExpressionBuilder.withOperator(SQLUnaryNumberOperator.ABSOLUTE).withExpression(this).build();
    }
    
    /* -------------------------------------------------- Binary Operations -------------------------------------------------- */
    
    /**
     * Returns the addition with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression add(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.ADDITION).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the subtraction with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression subtract(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.SUBTRACTION).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the multiplication with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression multiply(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.MULTIPLICATION).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the division with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression divide(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.DIVISION).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the integer division with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression divideInteger(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.INTEGER_DIVISION).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns the modulo with the given number expression.
     */
    @Pure
    public default @Nonnull SQLBinaryNumberExpression modulo(@Nonnull SQLNumberExpression expression) {
        return SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.MODULO).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /* -------------------------------------------------- Comparison Operations -------------------------------------------------- */
    
    /**
     * Returns an equal comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression equal(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns an unequal comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression unequal(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.UNEQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a greater or equal comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression greaterOrEqual(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER_OR_EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a greater comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression greater(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a less or equal comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression lessOrEqual(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS_OR_EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a less comparison with the given number expression.
     */
    @Pure
    public default @Nonnull SQLNumberComparisonBooleanExpression less(@Nonnull SQLNumberExpression expression) {
        return SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /* -------------------------------------------------- Between -------------------------------------------------- */
    
    /**
     * Returns a range expression where this expression has to be between the given start and stop expressions.
     */
    @Pure
    public default @Nonnull SQLNumberInRangeBooleanExpression between(@Nonnull SQLNumberExpression start, @Nonnull SQLNumberExpression stop) {
        return SQLNumberInRangeBooleanExpressionBuilder.withExpression(this).withStartValue(start).withStopValue(stop).build();
    }
    
}
