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
package net.digitalid.database.dialect.statement.select.unordered.simple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumn;

/**
 * This SQL node represents a group clause for an SQL select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLGroupClause extends SQLNode {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the columns used in the group-by clause.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumn> getColumns();
    
    /**
     * Returns the expression used in the having clause.
     */
    @Pure
    public @Nullable SQLBooleanExpression getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(" GROUP BY ");
        dialect.unparse(getColumns(), unit, string);
        final @Nullable SQLBooleanExpression expression = getExpression();
        if (expression != null) {
            string.append(" HAVING ");
            dialect.unparse(expression, unit, string);
        }
    }
    
}
