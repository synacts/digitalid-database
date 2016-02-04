package net.digitalid.database.testing;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.exceptions.internal.UncoveredCaseException;
import net.digitalid.utility.testing.TestingBase;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.DatabaseInstance;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.testing.h2.H2JDBCDatabaseInstance;

/**
 *
 */
public class SQLTestBase extends TestingBase {
    
    private static Server server;
    
    @BeforeClass
    public static void setUpSQL() throws Exception {
        server = Server.createTcpServer();
        server.start();
        H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS " + TestHost.SCHEMA_NAME + ";mode=MySQL;");
        //H2JDBCDatabaseInstance h2Database = H2JDBCDatabaseInstance.get("jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS " + TestHost.SCHEMA_NAME + ";mode=MySQL;");
        Database.initialize(h2Database);
    }
    
    @AfterClass
    public static void tearDownSQL() throws Exception {
        server.shutdown();
    }
    
    /**
     * A generic test that verifies that a connection to the database could be established, and that a table could be created by issuing an SQL statement.
     */
    @Test
    public void shouldConnectToDatabase() throws Exception {
        DatabaseInstance instance = Database.getInstance();
        instance.execute("CREATE TABLE blubb");
        SelectionResult tableExistsQuery = instance.executeSelect("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'BLUBB'");
        tableExistsQuery.moveToFirstRow();
        Assert.assertSame(1, tableExistsQuery.getInteger32());
    }
    
    protected void assertTableExists(@Nonnull String tableName, @Nonnull String schema) throws EntryNotFoundException, FailedNonCommittingOperationException {
        final @Nonnull String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableName.toUpperCase() + "' and table_schema = '" + schema.toUpperCase() + "'";
        DatabaseInstance instance = Database.getInstance();
        SelectionResult tableExistsQuery = instance.executeSelect(query);
        tableExistsQuery.moveToFirstRow();
        Assert.assertSame("Table does not exist (" + query + ")", 1, tableExistsQuery.getInteger32());
    }
    
    protected enum UpdateAction {
        CASCADE(0),
        RESTRICT(1);
        
        final int i;
        
        UpdateAction(int i) {
            this.i = i;
        }
        
        public static @Nonnull UpdateAction get(int i) {
            for (UpdateAction action : UpdateAction.values()) {
                if (action.i == i) {
                    return action;
                }
            }
            throw UncoveredCaseException.with(i);
        }
    }
    
    protected enum DeleteAction {
        CASCADE(0),
        RESTRICT(1);
        
        final int i;
        
        DeleteAction(int i) {
            this.i = i;
        }
        
        public static @Nonnull DeleteAction get(int i) {
            for (DeleteAction action : DeleteAction.values()) {
                if (action.i == i) {
                    return action;
                }
            }
            throw UncoveredCaseException.with(i);
        }
    }
    
    protected void assertTableReferences(@Nonnull String tableName, @Nonnull String schema, @Nonnull String column, @Nonnull String referencedTable, @Nonnull String referencedColumn, @Nonnull UpdateAction updateAction, @Nonnull DeleteAction deleteAction) throws FailedNonCommittingOperationException, EntryNotFoundException {
        final @Nonnull String query = "SELECT PKTABLE_NAME, PKCOLUMN_NAME, FKCOLUMN_NAME, UPDATE_RULE, DELETE_RULE FROM INFORMATION_SCHEMA.CROSS_REFERENCES WHERE FKTABLE_SCHEMA = '" + schema.toUpperCase() + "' AND FKTABLE_NAME = '" + tableName.toUpperCase() + "'";
        final @Nonnull DatabaseInstance instance = Database.getInstance();
        final @Nonnull SelectionResult tableReferencesResult = instance.executeSelect(query);
        
        tableReferencesResult.moveToFirstRow();
        final @Nullable String pkTableName = tableReferencesResult.getString();
        final @Nullable String pkColumnName = tableReferencesResult.getString();
        final @Nullable String fkColumnName = tableReferencesResult.getString();
        final int updateRule = tableReferencesResult.getInteger32();
        final int deleteRule = tableReferencesResult.getInteger32();
    
        Assert.assertEquals("Expected reference definition on column '" + column + "', but got '" + fkColumnName + "'", column, fkColumnName);
        Assert.assertEquals("Expected referenced table '" + referencedTable + "', but got '" + pkTableName + "'", referencedTable, pkTableName);
        Assert.assertEquals("Expected referenced column '" + referencedColumn + "', but got '" + pkColumnName + "'", referencedColumn, pkColumnName);
        Assert.assertSame("Expected UPDATE action '" + updateAction + "', but got '" + UpdateAction.get(updateRule) + "'", updateAction.i, updateRule);
        Assert.assertSame("Expected DELETE action '" + deleteAction + "', but got '" + DeleteAction.get(deleteRule) + "'", deleteAction.i, deleteRule);
    }
    
    protected void assertTableHasColumns(@Nonnull String tableName, @Nonnull String schema, @Nonnull @NonNullableElements Map<String, String[]> expectedResults) throws FailedNonCommittingOperationException, EntryNotFoundException {
        final @Nonnull String query = "SHOW COLUMNS FROM " + schema.toUpperCase() + "." + tableName.toUpperCase();
        final @Nonnull DatabaseInstance instance = Database.getInstance();
        final @Nonnull SelectionResult tableColumnsQuery = instance.executeSelect(query);
    
        tableColumnsQuery.moveToFirstRow();
        final @Nonnull String constraintQuery = "SELECT CHECK_CONSTRAINT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName.toUpperCase() + "' AND COLUMN_NAME = ";
        do {
            @Nullable String field = tableColumnsQuery.getString();
            Assert.assertNotNull("A column name was expected, but none defined", field);
            Assert.assertTrue("Column name '" + field + "' is unexpected", expectedResults.containsKey(field));
            @Nonnull String[] column = expectedResults.get(field);
            
            @Nullable String type = tableColumnsQuery.getString();
            Assert.assertNotNull("A column type was expected, but none defined", field);
            Assert.assertEquals(column[0], type);
            
            if (column.length > 1) {
                @Nullable String nullness = tableColumnsQuery.getString();
                Assert.assertEquals(column[1], nullness);
            }
            if (column.length > 2) {
                @Nullable String key = tableColumnsQuery.getString();
                Assert.assertEquals(column[2], key);
            }
            if (column.length > 3) {
                @Nullable String defaultValue = tableColumnsQuery.getString();
                Assert.assertEquals(column[3], defaultValue);
            }
            if (column.length > 4) {
                String constraintQueryString = constraintQuery + "'" + field + "'";
                final @Nonnull SelectionResult constraintResult = instance.executeSelect(constraintQueryString);
                constraintResult.moveToFirstRow();
                @Nullable String constraint = constraintResult.getString();
                Assert.assertNotNull("A constraint was expected, but none defined.", constraint);
                Assert.assertEquals(column[4], constraint);
            }
        } while (tableColumnsQuery.moveToNextRow());
    }
    
    protected void assertRowCount(@Nonnull Table table, long rowCount) throws FailedNonCommittingOperationException, EntryNotFoundException {
        final @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + table.getName().getValue().toUpperCase();
        final @Nonnull DatabaseInstance instance = Database.getInstance();
        final @Nonnull SelectionResult rowCountResult = instance.executeSelect(rowCountQuery);
        rowCountResult.moveToFirstRow();
        Assert.assertSame("Table '" + table + "' does not contain " + rowCount + " rows.", rowCount, rowCountResult.getInteger64());
    }
    
    protected void assertTableContains(@Nonnull Table table, @Nonnull @NonNullableElements String[][] values) throws EntryNotFoundException, FailedNonCommittingOperationException {
        final @Nonnull String tableName = table.getName().getValue();
        final @Nonnull DatabaseInstance instance = Database.getInstance();
        for (String[] value : values) {
            final @Nonnull String columnName = value[0];
            final @Nonnull String columnValue = value[1];
            final @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + tableName.toUpperCase() + " WHERE " + columnName.toUpperCase() + " = " + columnValue;
            final @Nonnull SelectionResult rowCountResult = instance.executeSelect(rowCountQuery);
            rowCountResult.moveToFirstRow();
            Assert.assertSame("Table '" + table + "' does not contain column " + columnName + " = " + columnValue, 1L, rowCountResult.getInteger64());
        }
    }
     
}
