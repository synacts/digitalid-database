package net.digitalid.database.dialect.statement.delete;

import javax.annotation.Nonnull;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.subject.site.SimpleSite;

import org.junit.Test;

public class SQLDeleteStatementTest extends SQLStatementTest {
    
    @Test
    public void testDeleteStatement() {
        final @Nonnull SQLNumberComparisonBooleanExpression whereClause = firstColumn.equal(SQLParameter.NUMBER);
        final @Nonnull SQLDeleteStatement deleteStatement = SQLDeleteStatementBuilder.withTable(qualifiedTable).withWhereClause(whereClause).build();
        assertEquals("DELETE FROM \"default\".\"test_table\" WHERE (\"first_column\") = (?)", SQLDialect.unparse(deleteStatement, SimpleSite.INSTANCE));
    }
    
}
