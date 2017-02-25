package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.immutable.ImmutableList;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteralBuilder;
import net.digitalid.database.dialect.expression.string.SQLStringLiteral;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

public class SQLInsertStatementTest extends SQLStatementTest {
    
    @Test
    public void testInsertStatement() {
        final @Nonnull SQLNumberLiteral firstExpression = SQLNumberLiteralBuilder.withValue(8l).build();
        final @Nonnull SQLBooleanLiteral secondExpression = SQLBooleanLiteral.TRUE;
        final @Nonnull SQLStringLiteral thirdExpression = SQLStringLiteralBuilder.withString("name").build();
        final @Nonnull SQLExpressions expressions = SQLExpressionsBuilder.withExpressions(ImmutableList.withElements(firstExpression, secondExpression, thirdExpression)).build();
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(expressions, expressions)).build();
        final @Nonnull SQLInsertStatement insertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(columns).withValues(rows).withReplacing(true).build();
        assertThat(SQLDialect.unparse(insertStatement, Unit.DEFAULT)).isEqualTo("INSERT OR REPLACE INTO \"default\".\"test_table\" (\"first_column\", \"second_column\", \"third_column\") VALUES (8.0, TRUE, \"name\"), (8.0, TRUE, \"name\")");
    }
    
}
