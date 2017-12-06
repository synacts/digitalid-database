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
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.table.SQLTable;

/**
 * An SQL qualified column.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLQualifiedColumn extends SQLColumn {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table of this qualified column.
     */
    @Pure
    public @Nonnull SQLTable getTable();
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Returns the column of this qualified column.
     */
    @Pure
    public @Nonnull SQLColumnName getColumn();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getTable(), unit, string);
        string.append(".");
        dialect.unparse(getColumn(), unit, string);
    }
    
}
