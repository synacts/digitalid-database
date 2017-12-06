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
package net.digitalid.database.dialect.statement.update;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;

/**
 * This type models an assignment in an update statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLAssignment extends SQLNode {
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Returns the column to which the given expression is assigned.
     */
    @Pure
    public @Nonnull SQLColumnName getColumn();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression that is assigned to the given column.
     */
    @Pure
    public @Nonnull SQLExpression getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getColumn(), unit, string);
        string.append(" = ");
        dialect.unparse(getExpression(), unit, string);
    }
    
}
