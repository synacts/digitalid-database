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
package net.digitalid.database.dialect.statement.select.ordered;

import javax.annotation.Nonnull;

import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.simple.SQLSimpleSelectStatementBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.columns.SQLAllColumnsBuilder;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSource;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLTableSourceBuilder;

import org.junit.Test;

public class SQLOrderedSelectStatementTest extends SQLStatementTest {
    
    @Test
    public void testOrderedSelectStatement() {
        final @Nonnull SQLTableSource tableSource = SQLTableSourceBuilder.withSource(qualifiedTable).build();
        final @Nonnull SQLSimpleSelectStatement simpleSelectStatement = SQLSimpleSelectStatementBuilder.withColumns(ImmutableList.withElements(SQLAllColumnsBuilder.build())).withSources(ImmutableList.withElements(tableSource)).build();
        final @Nonnull SQLOrderingTerm orderingTerm = SQLOrderingTermBuilder.withExpression(firstColumn).withAscending(false).build();
        final @Nonnull SQLOrderedSelectStatement selectStatement = SQLOrderedSelectStatementBuilder.withSelectStatement(simpleSelectStatement).withOrders(ImmutableList.withElements(orderingTerm)).withLimit(10).withOffset(20).build();
        assertThat(SQLDialect.unparse(selectStatement, Unit.DEFAULT)).isEqualTo("SELECT * FROM (\"default\".\"test_table\") ORDER BY \"first_column\" DESC LIMIT 10 OFFSET 20");
    }
    
}
