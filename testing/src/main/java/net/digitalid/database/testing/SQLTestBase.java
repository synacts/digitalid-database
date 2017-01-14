package net.digitalid.database.testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.set.FreezableHashSetBuilder;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.exceptions.CaseException;
import net.digitalid.utility.testing.RootTest;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.table.TableImplementation;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedSQLValueRecoveryException;
import net.digitalid.database.exceptions.state.row.EntryNotFoundException;
import net.digitalid.database.interfaces.DatabaseUtility;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.subject.site.SimpleSite;
import net.digitalid.database.subject.site.Site;
import net.digitalid.database.testing.h2.H2Dialect;
import net.digitalid.database.testing.h2.H2JDBCDatabaseInstance;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * The base class for all unit tests that interact with a database.
 */
public class SQLTestBase extends RootTest {
    
    private static Server server;
    
    /* -------------------------------------------------- Set Up -------------------------------------------------- */
    
    private static boolean initialized = false;
    
    /**
     * In order to inspect the database during debugging, set this boolean to false in a static block of the subclass.
     * Start '~/.m2/repository/com/h2database/h2/1.4.190/h2-1.4.190.jar' before running the test cases, which opens a
     * browser window, and connect to the database with the address below and both user and password set to 'sa'.
     */
    @SuppressWarnings("ProtectedField")
    protected static boolean runInMemory = true;
    
    @Impure
    @BeforeClass
    public static void setUpSQL() throws Exception {
        if (!initialized) {
            SQLDialect.dialect.set(new H2Dialect());
            DatabaseUtility.initialize(H2JDBCDatabaseInstance.get("jdbc:h2:" + (runInMemory ? "" : "tcp://localhost:9092/") + "mem:test;" + (runInMemory ? "" : "DB_CLOSE_DELAY=-1;") + "INIT=CREATE SCHEMA IF NOT EXISTS " + SimpleSite.INSTANCE.getSchemaName() + ";mode=MySQL;"));
            server = Server.createTcpServer();
            initialized = true;
        }
        server.start();
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    @Impure
    public static void dropTable(@Nonnull String tableName, @Nonnull Site site) throws FailedNonCommittingOperationException {
        Database instance = DatabaseUtility.getInstance();
        instance.execute("DROP TABLE " + site.getSchemaName() + "." + tableName.toLowerCase());
    }
    
    @Impure
    public static void dropTable(@Nonnull Converter<?, ?> converter, @Nonnull Site site) throws FailedNonCommittingOperationException {
        dropTable(converter.getTypeName(), site);
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    @Impure
    public static void deleteFromTable(@Nonnull String tableName, @Nonnull Site site) throws FailedNonCommittingOperationException {
        Database instance = DatabaseUtility.getInstance();
        instance.execute("DELETE FROM " + site.getSchemaName() + "." + tableName.toLowerCase());
    }
    
    @Impure
    public static void deleteFromTable(@Nonnull Converter<?, ?> converter, @Nonnull Site site) throws FailedNonCommittingOperationException {
        deleteFromTable(converter.getTypeName(), site);
    }
    
    /* -------------------------------------------------- Tear Down -------------------------------------------------- */
    
    @Impure
    @AfterClass
    public static void tearDownSQL() throws Exception {
        server.shutdown();
    }
    
    /**
     * A generic test that verifies that a connection to the database could be established, and that a table could be created by issuing an SQL statement.
     */
    @Pure
    @Test
    public void shouldConnectToDatabase() throws Exception {
        Database instance = DatabaseUtility.getInstance();
        instance.execute("CREATE TABLE IF NOT EXISTS blubb");
        SQLDecoder tableExistsQuery = instance.executeSelect("SELECT COUNT(*) FROM information_schema.tables WHERE table_name = 'BLUBB'");
        tableExistsQuery.moveToFirstRow();
        Assert.assertSame(1, tableExistsQuery.getInteger32());
    }
    
    @Pure
    protected void assertTableExists(@Nonnull String tableName, @Nonnull String schema) throws EntryNotFoundException, FailedNonCommittingOperationException, FailedSQLValueRecoveryException {
        final @Nonnull String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableName.toLowerCase() + "' and table_schema = '" + schema.toLowerCase() + "'";
        Database instance = DatabaseUtility.getInstance();
        SQLDecoder tableExistsQuery = instance.executeSelect(query);
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
        
        @Pure
        public static @Nonnull UpdateAction get(int i) {
            for (UpdateAction action : UpdateAction.values()) {
                if (action.i == i) {
                    return action;
                }
            }
            throw CaseException.with("UpdateAction", i);
        }
    }
    
    protected enum DeleteAction {
        CASCADE(0),
        RESTRICT(1);
        
        final int i;
        
        DeleteAction(int i) {
            this.i = i;
        }
    
        @Pure
        public static @Nonnull DeleteAction get(int i) {
            for (DeleteAction action : DeleteAction.values()) {
                if (action.i == i) {
                    return action;
                }
            }
            throw CaseException.with("DeleteAction", i);
        }
    }
    
    @Pure
    protected void assertTableReferences(@Nonnull String tableName, @Nonnull String schema, @Nonnull String column, @Nonnull String referencedTable, @Nonnull String referencedColumn, @Nonnull UpdateAction updateAction, @Nonnull DeleteAction deleteAction) throws FailedNonCommittingOperationException, EntryNotFoundException, FailedSQLValueRecoveryException {
        final @Nonnull String query = "SELECT PKTABLE_NAME, PKCOLUMN_NAME, FKCOLUMN_NAME, UPDATE_RULE, DELETE_RULE FROM INFORMATION_SCHEMA.CROSS_REFERENCES WHERE FKTABLE_SCHEMA = '" + schema.toUpperCase() + "' AND FKTABLE_NAME = '" + tableName.toUpperCase() + "'";
        final @Nonnull Database instance = DatabaseUtility.getInstance();
        final @Nonnull SQLDecoder tableReferencesResult = instance.executeSelect(query);
        
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
    
    @Pure
    protected void assertTableHasColumns(@Nonnull String tableName, @Nonnull String schema, @Nonnull Map<@Nonnull String, @Nonnull String[]> expectedResults) throws FailedNonCommittingOperationException, EntryNotFoundException, FailedSQLValueRecoveryException {
        final @Nonnull String query = "SHOW COLUMNS FROM " + schema.toLowerCase() + "." + tableName.toLowerCase();
        final @Nonnull Database instance = DatabaseUtility.getInstance();
        final @Nonnull SQLDecoder tableColumnsQuery = instance.executeSelect(query);
    
        tableColumnsQuery.moveToFirstRow();
        final @Nonnull String constraintQuery = "SELECT CHECK_CONSTRAINT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + tableName.toLowerCase() + "' AND COLUMN_NAME = ";
        final @Nonnull Set<String> checkedSet = FreezableHashSetBuilder.build();
        do {
            tableColumnsQuery.moveToFirstColumn();
            @Nullable String field = tableColumnsQuery.getString();
            Assert.assertNotNull("A column name was expected, but none defined", field);
            Assert.assertTrue("Column name '" + field + "' is unexpected", expectedResults.containsKey(field));
            @Nonnull String[] column = expectedResults.get(field);
            checkedSet.add(field);
            
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
                final @Nonnull SQLDecoder constraintResult = instance.executeSelect(constraintQueryString);
                constraintResult.moveToFirstRow();
                @Nullable String constraint = constraintResult.getString();
                Assert.assertNotNull("A constraint was expected, but none defined.", constraint);
                Assert.assertEquals(column[4], constraint);
            }
        } while (tableColumnsQuery.moveToNextRow());
        
        expectedResults.keySet().removeAll(checkedSet);
        if (expectedResults.size() > 0) {
            Assert.fail("Column name(s) '" + expectedResults.keySet() + "' not declared.");
        }
    }
    
    @Pure
    protected static void assertRowCount(@Nonnull String tableName, long rowCount) throws FailedNonCommittingOperationException, EntryNotFoundException, FailedSQLValueRecoveryException {
        final @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + tableName.toLowerCase();
        final @Nonnull Database instance = DatabaseUtility.getInstance();
        final @Nonnull SQLDecoder rowCountResult = instance.executeSelect(rowCountQuery);
        rowCountResult.moveToFirstRow();
        Assert.assertSame("Table '" + tableName + "' does not contain " + rowCount + " rows.", rowCount, rowCountResult.getInteger64());
    }
    
    @Pure
    protected static void assertRowCount(@Nonnull TableImplementation table, @Nonnull Site site, long rowCount) throws FailedNonCommittingOperationException, EntryNotFoundException, FailedSQLValueRecoveryException {
        assertRowCount(site.getSchemaName() + "." + table.getName(), rowCount);
    }
    
    @Pure
    protected static void assertTableContains(@Nonnull String tableName, @Nonnull @NonNullableElements Expected... expectedArray) throws EntryNotFoundException, FailedNonCommittingOperationException, FailedSQLValueRecoveryException {
        final @Nonnull Database instance = DatabaseUtility.getInstance();
        for (@Nonnull Expected expected : expectedArray) {
            @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + tableName.toLowerCase() ;
            if (expected.expectedColumnClauses.size() > 0) {
                rowCountQuery += " WHERE ";
            }
            String columnsInfo = "";
            for (int i = 0; i < expected.expectedColumnClauses.size(); i++) {
                ExpectedColumnClause expectedColumnClause = expected.expectedColumnClauses.get(i);
                final @Nonnull String columnName = expectedColumnClause.column;
                final @Nonnull String columnValue = expectedColumnClause.columnValue;
                rowCountQuery += columnName.toUpperCase() + " = " + columnValue;
                columnsInfo += columnName + " = " + columnValue;
                if (i < expected.expectedColumnClauses.size() - 1) {
                    rowCountQuery += " AND ";
                    columnsInfo += ", ";
                }
            }
            final @Nonnull SQLDecoder rowCountResult = instance.executeSelect(rowCountQuery);
            rowCountResult.moveToFirstRow();
            Assert.assertTrue("Table '" + tableName + "' does not contain column(s) " + columnsInfo, rowCountResult.getInteger64() >= 1L);
        }
    }
    
    @Pure
    protected static void assertTableContains(@Nonnull TableImplementation table, @Nonnull Site site, @Nonnull @NonNullableElements Expected... expectedArray) throws EntryNotFoundException, FailedNonCommittingOperationException, FailedSQLValueRecoveryException {
        final @Nonnull String tableName = table.getName();
        assertTableContains(site.getSchemaName() + "." + tableName, expectedArray);
    }
    
    // TODO: the following is still very ugly. Improve!
    public static class Expected {
        
        protected List<ExpectedColumnClause> expectedColumnClauses = new ArrayList<>();
        
        public static ExpectedColumnClause column(String columnName) {
            return new ExpectedColumnClause(columnName);
        }
        
    }
    
    public static class ExpectedColumnClause extends Expected implements Cloneable {
        
        public String column;
        public String columnValue;
        
        ExpectedColumnClause(@Nonnull String column) {
            this.column = column;
        }
        
        @Impure
        public ExpectedColumnClause value(String columnValue) {
            this.columnValue = columnValue;
            try {
                this.expectedColumnClauses.add((ExpectedColumnClause) this.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            return this;
        }
        
        @Impure
        public ExpectedColumnClause and(String columnName) {
            column = columnName;
            return this;
        }
        
    }
     
}
