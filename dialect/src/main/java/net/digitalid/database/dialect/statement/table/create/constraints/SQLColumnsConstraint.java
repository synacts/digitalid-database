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
package net.digitalid.database.dialect.statement.table.create.constraints;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.column.SQLColumnName;

/**
 * A table constraint that references a list of columns.
 * 
 * @see SQLForeignKeyConstraint
 * @see SQLPrimaryKeyConstraint
 * @see SQLUniqueConstraint
 */
@Immutable
public interface SQLColumnsConstraint extends SQLTableConstraint {
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> getColumns();
    
}
