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

import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.number.SQLLongLiteralBuilder;
import net.digitalid.database.dialect.statement.SQLStatementTest;

import org.junit.Test;

public class SQLUpdateStatementTest extends SQLStatementTest {
    
    @Test
    public void testSQLUpdateStatement() {
        final @Nonnull SQLAssignment assignment = SQLAssignmentBuilder.withColumn(thirdColumn).withExpression(SQLParameter.INSTANCE).build();
        final @Nonnull SQLUpdateStatement updateStatement = SQLUpdateStatementBuilder.withTable(qualifiedTable).withAssignments(ImmutableList.withElements(assignment)).withWhereClause(firstColumn.equal(SQLLongLiteralBuilder.withValue(8).build())).build();
        assertThat(SQLDialect.unparse(updateStatement, Unit.DEFAULT)).isEqualTo("UPDATE \"default\".\"test_table\" SET \"third_column\" = ? WHERE (\"first_column\") = (8)");
    }
    
}
