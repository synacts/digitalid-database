package net.digitalid.database.dialect.statement.table.drop;

import javax.annotation.Nonnull;

import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.SQLStatementTest;

import org.junit.Test;

public class SQLDropTableStatementTest extends SQLStatementTest {
    
    @Test
    public void testDropTableStatement() {
        final @Nonnull SQLDropTableStatement dropTableStatement = SQLDropTableStatementBuilder.withTable(qualifiedTable).build();
        assertThat(SQLDialect.unparse(dropTableStatement, Unit.DEFAULT)).isEqualTo("DROP TABLE IF EXISTS \"default\".\"test_table\"");
    }
    
}
