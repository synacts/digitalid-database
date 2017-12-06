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
package net.digitalid.database.dialect.statement.select.unordered;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.compound.SQLCompoundOperator;
import net.digitalid.database.dialect.statement.select.unordered.compound.SQLCompoundSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.compound.SQLCompoundSelectStatementBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatement;

/**
 * An SQL select statement without an order or limit clause.
 * 
 * @see SQLSimpleSelectStatement
 * @see SQLCompoundSelectStatement
 */
@Immutable
public interface SQLUnorderedSelectStatement extends SQLSelectStatement {
    
    /* -------------------------------------------------- Compound Operations -------------------------------------------------- */
    
    /**
     * Combines the results of this and the given select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement union(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return SQLCompoundSelectStatementBuilder.withOperator(SQLCompoundOperator.UNION).withLeftExpression(this).withRightExpression(selectStatement).build();
    }
    
    /**
     * Intersects the results of this and the given select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement intersect(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return SQLCompoundSelectStatementBuilder.withOperator(SQLCompoundOperator.INTERSECT).withLeftExpression(this).withRightExpression(selectStatement).build();
    }
    
    /**
     * Excludes the results of given select statement from this select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement except(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return SQLCompoundSelectStatementBuilder.withOperator(SQLCompoundOperator.EXCEPT).withLeftExpression(this).withRightExpression(selectStatement).build();
    }
    
}
