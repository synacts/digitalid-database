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

import net.digitalid.database.dialect.expression.SQLUnaryOperator;

/**
 * This class enumerates the supported aggregate operators.
 * 
 * @see SQLAggregateNumberExpression
 */
@Immutable
public enum SQLAggregateOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the average value of all non-null values in the column.
     */
    AVG("AVG"),
    
    /**
     * This operator returns the number of non-null values in the column.
     */
    COUNT("COUNT"),
    
    /**
     * This operator returns the maximum value in the column.
     */
    MAX("MAX"),
    
    /**
     * This operator returns the minimum value in the column.
     */
    MIN("MIN"),
    
    /**
     * This operator returns the sum of all values in the column.
     */
    SUM("SUM");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLAggregateOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
