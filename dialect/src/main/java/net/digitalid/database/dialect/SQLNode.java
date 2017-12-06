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
package net.digitalid.database.dialect;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLOperator;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.identifier.table.SQLTable;
import net.digitalid.database.dialect.statement.SQLStatementNode;
import net.digitalid.database.dialect.statement.insert.SQLExpressions;
import net.digitalid.database.dialect.statement.insert.SQLValues;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLResultColumnOrAllColumns;

/**
 * All SQL syntax tree nodes implement this interface.
 * 
 * @see SQLExpression
 * @see SQLOperator
 * @see SQLIdentifier
 * @see SQLTable
 * @see SQLStatementNode
 * @see SQLExpressions
 * @see SQLValues
 * @see SQLResultColumnOrAllColumns
 */
@Immutable
public interface SQLNode extends RootInterface {
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    /**
     * Appends this node as SQL in the given dialect at the given unit to the given string.
     */
    @Pure
    public void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string);
    
}
