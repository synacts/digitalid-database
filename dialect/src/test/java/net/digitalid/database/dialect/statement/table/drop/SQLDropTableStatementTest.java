package net.digitalid.database.dialect.statement.table.drop;

import javax.annotation.Nonnull;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLStatementTest;
import net.digitalid.database.subject.site.SimpleSite;

import org.junit.Test;

public class SQLDropTableStatementTest extends SQLStatementTest {
    
    @Test
    public void testDropTableStatement() {
        final @Nonnull SQLDropTableStatement dropTableStatement = SQLDropTableStatementBuilder.withTable(qualifiedTable).build();
        assertEquals("DROP TABLE IF EXISTS \"default\".\"test_table\"", SQLDialect.unparse(dropTableStatement, SimpleSite.INSTANCE));
    }
    
}
