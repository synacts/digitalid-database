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
package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLSelectionExistsBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLSelectionExistsBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;
import net.digitalid.database.dialect.statement.SQLStatementNode;
import net.digitalid.database.dialect.statement.select.ordered.SQLOrderedSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.SQLUnorderedSelectStatement;

/**
 * This SQL node represents an SQL select statement.
 * 
 * @see SQLOrderedSelectStatement
 * @see SQLUnorderedSelectStatement
 */
@Immutable
public interface SQLSelectStatement extends SQLStatementNode, SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {
    
    /* -------------------------------------------------- Exists -------------------------------------------------- */
    
    /**
     * Returns a boolean expression which checks that result set of this select statement is not empty.
     */
    @Pure
    public default @Nonnull SQLSelectionExistsBooleanExpression exists() {
        return SQLSelectionExistsBooleanExpressionBuilder.withSelection(this).build();
    }
    
}
