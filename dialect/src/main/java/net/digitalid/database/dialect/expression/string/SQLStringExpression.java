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
package net.digitalid.database.dialect.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLStringComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLStringComparisonBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLStringInRangeBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLStringInRangeBooleanExpressionBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumn;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;

/**
 * A string expression evaluates to a string.
 * 
 * @see SQLStringLiteral
 * @see SQLVariadicStringExpression
 * 
 * @see SQLSelectStatement
 * @see SQLParameter
 * @see SQLColumn
 */
@Immutable
public interface SQLStringExpression extends SQLExpression {
    
    /* -------------------------------------------------- Variadic Operations -------------------------------------------------- */
    
    /**
     * Returns the concatenation with the given string expression.
     */
    @Pure
    public default @Nonnull SQLVariadicStringExpression concat(@Nonnull SQLStringExpression expression) {
        return SQLVariadicStringExpressionBuilder.withOperator(SQLVariadicStringOperator.CONCAT).withExpressions(ImmutableList.withElements(this, expression)).build();
    }
    
    /* -------------------------------------------------- Comparison Operations -------------------------------------------------- */
    
    /**
     * Returns an equal comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression equal(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns an unequal comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression unequal(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.UNEQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a greater or equal comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression greaterOrEqual(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER_OR_EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a greater comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression greater(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a less or equal comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression lessOrEqual(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS_OR_EQUAL).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /**
     * Returns a less comparison with the given string expression.
     */
    @Pure
    public default @Nonnull SQLStringComparisonBooleanExpression less(@Nonnull SQLStringExpression expression) {
        return SQLStringComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS).withLeftExpression(this).withRightExpression(expression).build();
    }
    
    /* -------------------------------------------------- Between -------------------------------------------------- */
    
    /**
     * Returns a range expression where this expression has to be between the given start and stop expressions.
     */
    @Pure
    public default @Nonnull SQLStringInRangeBooleanExpression between(@Nonnull SQLStringExpression start, @Nonnull SQLStringExpression stop) {
        return SQLStringInRangeBooleanExpressionBuilder.withExpression(this).withStartValue(start).withStopValue(stop).build();
    }
    
}
