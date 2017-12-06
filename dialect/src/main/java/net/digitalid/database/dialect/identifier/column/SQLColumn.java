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
package net.digitalid.database.dialect.identifier.column;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLAggregateNumberExpression;
import net.digitalid.database.dialect.expression.number.SQLAggregateNumberExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLAggregateOperator;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;

/**
 * An SQL column.
 * 
 * @see SQLColumnName
 * @see SQLColumnAlias
 * @see SQLQualifiedColumn
 */
@Immutable
public interface SQLColumn extends SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {
    
    /* -------------------------------------------------- Aggregation -------------------------------------------------- */
    
    /**
     * Returns a number expression which returns the average value of all non-null values in this column.
     */
    @Pure
    public default @Nonnull SQLAggregateNumberExpression avg() {
        return SQLAggregateNumberExpressionBuilder.withOperator(SQLAggregateOperator.AVG).withColumn(this).build();
    }
    
    /**
     * Returns a number expression which returns the number of non-null values in this column.
     */
    @Pure
    public default @Nonnull SQLAggregateNumberExpression count() {
        return SQLAggregateNumberExpressionBuilder.withOperator(SQLAggregateOperator.COUNT).withColumn(this).build();
    }
    
    /**
     * Returns a number expression which returns the maximum value in this column.
     */
    @Pure
    public default @Nonnull SQLAggregateNumberExpression max() {
        return SQLAggregateNumberExpressionBuilder.withOperator(SQLAggregateOperator.MAX).withColumn(this).build();
    }
    
    /**
     * Returns a number expression which returns the minimum value in this column.
     */
    @Pure
    public default @Nonnull SQLAggregateNumberExpression min() {
        return SQLAggregateNumberExpressionBuilder.withOperator(SQLAggregateOperator.MIN).withColumn(this).build();
    }
    
    /**
     * Returns a number expression which returns the sum of all values in this column.
     */
    @Pure
    public default @Nonnull SQLAggregateNumberExpression sum() {
        return SQLAggregateNumberExpressionBuilder.withOperator(SQLAggregateOperator.SUM).withColumn(this).build();
    }
    
}
