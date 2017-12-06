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
package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.statement.SQLTableStatement;

/**
 * An SQL insert statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLInsertStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the columns into which the given values are inserted.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> getColumns();
    
    /* -------------------------------------------------- Values -------------------------------------------------- */
    
    /**
     * Returns the values which are inserted into the given columns.
     */
    @Pure
    public @Nonnull SQLValues getValues();
    
    /* -------------------------------------------------- Conflict -------------------------------------------------- */
    
    /**
     * Returns the conflict clause for handling constraint violations.
     */
    @Pure
    @Default("SQLConflictClause.ABORT")
    public @Nonnull SQLConflictClause getConflictClause();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getConflictClause(), unit, string);
        string.append(" INTO ");
        dialect.unparse(getTable(), unit, string);
        string.append(" (");
        dialect.unparse(getColumns(), unit, string);
        string.append(") ");
        dialect.unparse(getValues(), unit, string);
    }
    
}
