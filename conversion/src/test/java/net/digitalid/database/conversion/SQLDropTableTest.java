package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

/**
 *
 */
public class SQLDropTableTest extends SQLTestBase {
    
    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
    @Test
    public void shouldDropTableWithSingleBooleanColumn() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    
        SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableDoesNotExist(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    }
    
}
