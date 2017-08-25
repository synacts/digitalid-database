package net.digitalid.database.testing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.testing.UtilityTest;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.jdbc.JDBCDatabaseBuilder;
import net.digitalid.database.testing.assertion.ExpectedColumnDeclarations;
import net.digitalid.database.testing.assertion.ExpectedTableConstraints;

import org.assertj.db.api.Assertions;
import org.assertj.db.api.ChangesAssert;
import org.assertj.db.api.RequestAssert;
import org.assertj.db.api.TableAssert;
import org.assertj.db.type.Changes;
import org.assertj.db.type.Request;
import org.assertj.db.type.Table;
import org.h2.Driver;
import org.junit.After;
import org.junit.Assert;

/**
 * The base class for all unit tests that interact with the database.
 * 
 * In order to inspect the database during debugging, set the system property 'debugH2' to true.
 * Start '~/.m2/repository/com/h2database/h2/1.4.190/h2-1.4.190.jar' before running the test cases, which opens a
 * browser window, and connect to the database with the address below and both user and password set to 'sa'.
 * 
 * Database URL: jdbc:h2:tcp://localhost:9092/mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS default
 * 
 * When building with Maven, run the following command to inspect the database: {@code mvn test -DdebugH2}.
 */
@Stateless
public class DatabaseTest extends UtilityTest {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the database.
     */
    @PureWithSideEffects
    @Initialize(target = Database.class)
    public static void initializeDatabase() throws SQLException {
        if (!Database.instance.isSet()) {
            final boolean debugH2 = Boolean.parseBoolean(System.getProperty("debugH2"));
            final @Nonnull String URL = "jdbc:h2:" + (debugH2 ? "tcp://localhost:9092/" : "") + "mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS " + Unit.DEFAULT.getName() + ";MODE=MySQL;";
            Database.instance.set(JDBCDatabaseBuilder.withDriver(new Driver()).withURL(URL).withUser("sa").withPassword("sa").build());
        }
    }
    
    /* -------------------------------------------------- Commit -------------------------------------------------- */
    
    /**
     * Commits the current transaction after each unit test.
     * This is important in order to debug the stored data.
     */
    @After
    @Committing
    @PureWithSideEffects
    public void commit() throws DatabaseException {
        Database.commit();
    }
    
    /* -------------------------------------------------- Assertions -------------------------------------------------- */
    
    /**
     * Returns an assertion about the given table.
     */
    @Pure
    public static @Nonnull TableAssert assertThat(@Nonnull Table table) {
        return Assertions.assertThat(table);
    }
    
    /**
     * Returns an assertion about the given request.
     */
    @Pure
    public static @Nonnull RequestAssert assertThat(@Nonnull Request request) {
        return Assertions.assertThat(request);
    }
    
    /**
     * Returns an assertion about the given changes.
     */
    @Pure
    public static @Nonnull ChangesAssert assertThat(@Nonnull Changes changes) {
        return Assertions.assertThat(changes);
    }
    
    // TODO: Clean up the following mess!
    
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
    
    /* -------------------------------------------------- Existence -------------------------------------------------- */
    
    @Pure
    protected void assertTableExists(@Nonnull String tableName, @Nonnull String schema) throws DatabaseException {
        final @Nonnull String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableName.toLowerCase() + "' and table_schema = '" + schema.toLowerCase() + "'";
        final @Nonnull ResultSet resultSet = Database.instance.get().executeQuery(query);
        try {
            resultSet.next();
            Assert.assertSame("Table does not exist (" + query + ")", 1, resultSet.getInt(1));
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
    @Pure
    protected void assertTableDoesNotExist(@Nonnull String tableName, @Nonnull String schema) throws DatabaseException {
        final @Nonnull String query = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = '" + tableName.toLowerCase() + "' and table_schema = '" + schema.toLowerCase() + "'";
        final @Nonnull ResultSet resultSet = Database.instance.get().executeQuery(query);
        try {
            resultSet.next();
            Assert.assertSame("Table does exist (" + query + ")", 0, resultSet.getInt(1));
        } catch (SQLException e) {
            throw DatabaseExceptionBuilder.withCause(e).build();
        }
    }
    
    @Pure
    protected void assertTableReferences(@Nonnull ExpectedTableConstraints expectedTableConstraints) throws DatabaseException {
        expectedTableConstraints.assertTableConstraints(Database.instance.get());
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
        expectedTableConstraints.assertTableConstraints(Database.instance.get());
    }
    
    @Pure
    protected void assertTableHasExpectedColumnsDeclaration(@Nonnull String tableName, @Nonnull String schema, @Nonnull ExpectedColumnDeclarations expectedColumnDeclarations) throws DatabaseException {
        final @Nonnull String qualifiedTableName = schema + "." + tableName;
        expectedColumnDeclarations.assertColumnFieldsExist(qualifiedTableName, Database.instance.get());
        expectedColumnDeclarations.assertColumnConstraintsExist(tableName, Database.instance.get());
    }
    
    @Pure
    protected static void assertRowCount(@Nonnull String tableName, @Nonnull String schema, long rowCount) throws DatabaseException {
        final @Nonnull String rowCountQuery = "SELECT COUNT(*) AS count FROM " + schema + "." + tableName.toLowerCase();
        final @Nonnull ResultSet resultSet = Database.instance.get().executeQuery(rowCountQuery);
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
            final @Nonnull ResultSet rowCountResult = Database.instance.get().executeQuery(rowCountQuery);
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
