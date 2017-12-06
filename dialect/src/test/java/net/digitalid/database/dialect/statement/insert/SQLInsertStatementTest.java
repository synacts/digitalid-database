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

import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.number.SQLLongLiteralBuilder;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.expression.string.SQLStringLiteral;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.statement.SQLStatementTest;

import org.junit.Test;

public class SQLInsertStatementTest extends SQLStatementTest {
    
    @Test
    public void testInsertStatement() {
        final @Nonnull SQLNumberLiteral firstExpression = SQLLongLiteralBuilder.withValue(8).build();
        final @Nonnull SQLBooleanLiteral secondExpression = SQLBooleanLiteral.TRUE;
        final @Nonnull SQLStringLiteral thirdExpression = SQLStringLiteralBuilder.withString("name").build();
        final @Nonnull SQLExpressions expressions = SQLExpressionsBuilder.withExpressions(ImmutableList.withElements(firstExpression, secondExpression, thirdExpression)).build();
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(expressions, expressions)).build();
        final @Nonnull SQLInsertStatement insertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(columns).withValues(rows).withConflictClause(SQLConflictClause.REPLACE).build();
        assertThat(SQLDialect.unparse(insertStatement, Unit.DEFAULT)).isEqualTo("INSERT OR REPLACE INTO \"default\".\"test_table\" (\"first_column\", \"second_column\", \"third_column\") VALUES (8, TRUE, \"name\"), (8, TRUE, \"name\")");
    }
    
}
