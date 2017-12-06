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
package net.digitalid.database.dialect.statement.table.create;

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
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;

/**
 * An SQL create table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLCreateTableStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    /**
     * Returns the column declarations.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnDeclaration> getColumnDeclarations();
    
    /* -------------------------------------------------- Table Constraints -------------------------------------------------- */
    
    /**
     * Returns the optional table constraints.
     */
    @Pure
    public @Nullable @NonNullableElements @NonEmpty ImmutableList<? extends SQLTableConstraint> getTableConstraints();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("CREATE TABLE IF NOT EXISTS ");
        dialect.unparse(getTable(), unit, string);
        string.append(" (");
        dialect.unparse(getColumnDeclarations(), unit, string);
        final @Nullable @NonNullableElements @NonEmpty ImmutableList<? extends SQLTableConstraint> tableConstraints = getTableConstraints();
        if (tableConstraints != null) {
            string.append(", ");
            dialect.unparse(tableConstraints, unit, string);
        }
        string.append(")");
    }
    
}
