package net.digitalid.database.testing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.testing.RootTest;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarations;
import net.digitalid.database.testing.assertion.ExpectedTableConstraint;
import net.digitalid.database.testing.assertion.ExpectedTableConstraints;
import net.digitalid.database.testing.h2.H2JDBCDatabaseInstance;
import net.digitalid.database.unit.Unit;

import org.h2.tools.Server;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

/**
 * The base class for all unit tests that interact with a database.
 */
public class SQLTestBase extends RootTest {
    
    private static @Nonnull Server server;
    
    private static @Nonnull H2JDBCDatabaseInstance h2DatabaseInstance;
    
    /* -------------------------------------------------- Set Up -------------------------------------------------- */
    
    private static boolean initialized = false;
    
    /**
     * In order to inspect the database during debugging, set the system property "debugH2" to true.
     * Start '~/.m2/repository/com/h2database/h2/1.4.190/h2-1.4.190.jar' before running the test cases, which opens a
     * browser window, and connect to the database with the address below and both user and password set to 'sa'.
     * 
     * Database URL: jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS Default;mode=MySQL;
     * 
     * When building with Maven, run the following command to debug:
     * mvn test -DargLine="-DdebugH2=true"
     */
    @Impure
    @BeforeClass
    public static void setUpSQL() throws Exception {
        final boolean debugH2 = Boolean.parseBoolean(System.getProperty("debugH2"));
        if (!initialized) {
            h2DatabaseInstance = H2JDBCDatabaseInstance.get("jdbc:h2:" + (debugH2 ? "tcp://localhost:9092/" : "") + "mem:test;" + (debugH2 ? "DB_CLOSE_DELAY=-1;" : "") + "INIT=CREATE SCHEMA IF NOT EXISTS " + Unit.DEFAULT.getName()+ ";mode=MySQL;");
            Database.instance.set(h2DatabaseInstance);
            server = Server.createTcpServer();
            initialized = true;
        }
        server.start();
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    @Impure
    public static void dropTable(@Nonnull String tableName, @Nonnull Unit unit) throws DatabaseException {
        Database instance = Database.instance.get();
        // TODO: instance.execute("DROP TABLE " + unit.getName() + "." + tableName.toLowerCase());
    }
    
    @Impure
    public static void dropTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        dropTable(converter.getTypeName(), unit);
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    @Impure
    public static void deleteFromTable(@Nonnull String tableName, @Nonnull Unit unit) throws DatabaseException {
        Database instance = Database.instance.get();
        // TODO: instance.execute("DELETE FROM " + unit.getName() + "." + tableName.toLowerCase());
    }
    
    @Impure
    public static void deleteFromTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        deleteFromTable(converter.getTypeName(), unit);
    }
    
    /* -------------------------------------------------- Tear Down -------------------------------------------------- */
    
    @Impure
    @AfterClass
    public static void tearDownSQL() throws Exception {
        server.shutdown();
    }
    
    @Pure
    protected void assertTableExists(@Nonnull String tableName, @Nonnull String schema) throws DatabaseException {
        final @Nonnull String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableName.toLowerCase() + "' and table_schema = '" + schema.toLowerCase() + "'";
        Database instance = Database.instance.get();
        // TODO: SQLDecoder tableExistsQuery = instance.executeSelect(query);
        // TODO: tableExistsQuery.moveToFirstRow();
        // TODO: Assert.assertSame("Table does not exist (" + query + ")", 1, tableExistsQuery.decodeInteger32());
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
            throw CaseExceptionBuilder.withVariable("UpdateAction").withValue(i).build();
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
            throw CaseExceptionBuilder.withVariable("DeleteAction").withValue(i).build();
        }
    }
    
    @Pure
    protected void assertTableReferences(@Nonnull ExpectedTableConstraints expectedTableConstraints) throws DatabaseException {
        expectedTableConstraints.assertTableConstraints(h2DatabaseInstance);
        // TODO: final @Nonnull SQLDecoder tableReferencesResult = instance.executeSelect(query);
        // TODO:
//        tableReferencesResult.moveToFirstRow();
//        final @Nullable String pkTableName = tableReferencesResult.getString();
//        final @Nullable String pkColumnName = tableReferencesResult.getString();
//        final @Nullable String fkColumnName = tableReferencesResult.getString();
//        final int updateRule = tableReferencesResult.decodeInteger32();
//        final int deleteRule = tableReferencesResult.decodeInteger32();
//    
//        Assert.assertEquals("Expected reference definition on column '" + column + "', but got '" + fkColumnName + "'", column, fkColumnName);
//        Assert.assertEquals("Expected referenced table '" + referencedTable + "', but got '" + pkTableName + "'", referencedTable, pkTableName);
//        Assert.assertEquals("Expected referenced column '" + referencedColumn + "', but got '" + pkColumnName + "'", referencedColumn, pkColumnName);
//        Assert.assertSame("Expected UPDATE action '" + updateAction + "', but got '" + UpdateAction.get(updateRule) + "'", updateAction.i, updateRule);
//        Assert.assertSame("Expected DELETE action '" + deleteAction + "', but got '" + DeleteAction.get(deleteRule) + "'", deleteAction.i, deleteRule);
    }
    
    @Pure
    protected void assertTableHasExpectedTableConstraints(@Nonnull ExpectedTableConstraints expectedTableConstraints) throws DatabaseException {
        expectedTableConstraints.assertTableConstraints(h2DatabaseInstance);
    }
    
    @Pure
    protected void assertTableHasExpectedColumnsDeclaration(@Nonnull String tableName, @Nonnull String schema, @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations) throws DatabaseException {
        final @Nonnull String qualifiedTableName = schema + "." + tableName;
        expectedColumnDeclarations.assertColumnFieldsExist(qualifiedTableName, h2DatabaseInstance);
        expectedColumnDeclarations.assertColumnConstraintsExist(tableName, h2DatabaseInstance);
    }
    
    @Pure
    protected static void assertRowCount(@Nonnull String tableName, @Nonnull String schema, long rowCount) throws DatabaseException {
        final @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + schema + "." + tableName.toLowerCase();
        final @Nonnull ResultSet resultSet = h2DatabaseInstance.executeQuery(rowCountQuery);
        try {
            resultSet.next();
            Assert.assertSame("Table '" + tableName + "' does not contain " + rowCount + " rows.", rowCount, resultSet.getLong(1));
        } catch (SQLException e) {
            throw DatabaseExceptionBuilder.withCause(e).build();
        }
    }
    
//    @Pure
//    protected static void assertRowCount(@Nonnull TableImplementation table, @Nonnull Unit unit, long rowCount) throws DatabaseException {
//        assertRowCount(unit.getName() + "." + table.getName(), rowCount);
//    }
    
    @Pure
    protected static void assertTableContains(@Nonnull String tableName, @Nonnull String schema, @Nonnull @NonNullableElements Expected... expectedArray) throws DatabaseException {
        for (@Nonnull Expected expected : expectedArray) {
            @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + schema + "." + tableName.toLowerCase() ;
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
            final @Nonnull ResultSet rowCountResult = h2DatabaseInstance.executeQuery(rowCountQuery);
            try {
                rowCountResult.next();
                Assert.assertTrue("Table '" + tableName + "' does not contain column(s) " + columnsInfo, rowCountResult.getLong(1) >= 1L);
            } catch (SQLException e) {
                throw DatabaseExceptionBuilder.withCause(e).build();
            }
        }
    }
    
//    @Pure
//    protected static void assertTableContains(@Nonnull TableImplementation table, @Nonnull Unit unit, @Nonnull @NonNullableElements Expected... expectedArray) throws DatabaseException {
//        final @Nonnull String tableName = table.getName();
//        assertTableContains(unit.getName() + "." + tableName, expectedArray);
//    }
    
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
