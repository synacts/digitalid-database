package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.utility.storage.interfaces.Unit;

import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.DatabaseTest;

import org.junit.Test;

/**
 *
 */
public class SQLDropTableTest extends DatabaseTest {
    
    private static final @Nonnull Unit unit = Unit.DEFAULT;
    
    @Test
    public void shouldDropTableWithSingleBooleanColumn() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableExists(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    
        SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        assertTableDoesNotExist(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName());
    }
    
}
