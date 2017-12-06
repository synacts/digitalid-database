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
package net.digitalid.database.dialect.statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;

import net.digitalid.database.dialect.SQLDialectTest;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLImplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableAlias;
import net.digitalid.database.dialect.identifier.table.SQLTableAliasBuilder;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;

public class SQLStatementTest extends SQLDialectTest {
    
    public final @Nonnull SQLTableName table = SQLTableNameBuilder.withString("test_table").build();
    
    public final @Nonnull SQLTableAlias tableAlias = SQLTableAliasBuilder.withString("t").build();
    
    public final @Nonnull SQLQualifiedTable qualifiedTable = SQLImplicitlyQualifiedTableBuilder.withTable(table).build();
    
    public final @Nonnull SQLColumnName firstColumn = SQLColumnNameBuilder.withString("first_column").build();
    
    public final @Nonnull SQLColumnName secondColumn = SQLColumnNameBuilder.withString("second_column").build();
    
    public final @Nonnull SQLColumnName thirdColumn = SQLColumnNameBuilder.withString("third_column").build();
    
    public final @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLColumnName> columns = ImmutableList.withElements(firstColumn, secondColumn, thirdColumn);
    
}
