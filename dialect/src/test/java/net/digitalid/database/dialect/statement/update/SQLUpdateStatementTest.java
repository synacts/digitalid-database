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
