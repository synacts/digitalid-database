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
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;

/**
 * This type models a foreign key reference.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLReference extends SQLNode {
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    /**
     * Returns the table whose columns are referenced.
     */
    @Pure
    public @Nonnull SQLQualifiedTable getTable();
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    /**
     * Returns the referenced columns within the given table.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> getColumns();
    
    /* -------------------------------------------------- Delete Option -------------------------------------------------- */
    
    /**
     * Returns the referential action triggered on deletion.
     */
    @Pure
    public @Nullable SQLReferenceOption getDeleteOption();
    
    /* -------------------------------------------------- Update Option -------------------------------------------------- */
    
    /**
     * Returns the referential action triggered on update.
     */
    @Pure
    public @Nullable SQLReferenceOption getUpdateOption();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(" REFERENCES ");
        dialect.unparse(getTable(), unit, string);
        string.append(" (");
        dialect.unparse(getColumns(), unit, string);
        string.append(")");
        
        final @Nullable SQLReferenceOption deleteOption = getDeleteOption();
        if (deleteOption != null) {
            string.append(" ON DELETE ");
            dialect.unparse(deleteOption, unit, string);
        }
        
        final @Nullable SQLReferenceOption updateOption = getUpdateOption();
        if (updateOption != null) {
            string.append(" ON UPDATE ");
            dialect.unparse(updateOption, unit, string);
        }
    }
    
}
