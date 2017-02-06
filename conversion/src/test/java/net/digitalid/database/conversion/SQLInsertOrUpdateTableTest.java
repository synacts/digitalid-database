package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTable;
import net.digitalid.database.conversion.testenvironment.simple.SingleBooleanColumnTableConverter;
import net.digitalid.database.testing.SQLTestBase;
import net.digitalid.database.unit.Unit;

import org.junit.Test;

public class SQLInsertOrUpdateTableTest extends SQLTestBase {

    private static final @Nonnull Unit unit = Unit.DEFAULT;

    @Test
    public void shouldInsertOrUpdateSimpleBooleanTable() throws Exception {
        SQL.createTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        try {
            SQL.insertOrUpdate(SingleBooleanColumnTableConverter.INSTANCE, SingleBooleanColumnTable.get(true), unit);
    
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
    
            SQL.insertOrUpdate(SingleBooleanColumnTableConverter.INSTANCE, SingleBooleanColumnTable.get(true), unit);
    
            assertRowCount(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), 1);
            assertTableContains(SingleBooleanColumnTableConverter.INSTANCE.getTypeName(), unit.getName(), Expected.column("value").value("TRUE"));
        } finally {
            SQL.dropTable(SingleBooleanColumnTableConverter.INSTANCE, unit);
        }
    }
    
}
