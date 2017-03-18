package net.digitalid.database.sqlite.testbase;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.contracts.Ensure;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.testing.UtilityTest;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclarationBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatementBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLTypeBuilder;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.sqlite.SQLiteDialect;
import net.digitalid.database.unit.Unit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 */
public class SQLiteDatabaseTest extends UtilityTest {
    
    private final static @Nonnull String DB_CONNECTION_URL_PREFIX = "jdbc:sqlite:";
    
    private final static @Nonnull String DB_NAME = "test.db";
    
    protected final static @Nonnull String SCHEMA_NAME = "test";
    
    // nullable until setUp runs
    private static @Nullable File databaseFile;
    
    // nullable until getConnection is called
    private static @Nullable Connection connection;
    
    @Pure
    protected @Nonnull Connection createConnection() throws DatabaseException, SQLException {
        final @Nonnull Connection connection;
        Require.that(databaseFile != null).orThrow("The database file was never created.");
        try {
            connection = DriverManager.getConnection(DB_CONNECTION_URL_PREFIX + databaseFile.getAbsolutePath());
            // SQLite only supports SERIALIZABLE and READ_UNCOMMITTED. For testing purposes, it does not really matter which isolation level we are using. In production, the isolation level should probably be set to SERIALIZABLE to ensure that multiple threads with transactions to the database can never read uncommitted changes.
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            connection.setAutoCommit(true);
        } catch (@Nonnull SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
        Ensure.that(!connection.isClosed()).orThrow("The database connection should be open.");
        Ensure.that(!connection.isReadOnly()).orThrow("The database connection should be writable.");
        
        connection.createStatement().execute("ATTACH '" + databaseFile.getAbsolutePath() + "' AS " + SCHEMA_NAME);
        connection.setAutoCommit(false);
        Ensure.that(databaseFile.exists()).orThrow("Failed to create the database file $", databaseFile.getAbsolutePath());
        return connection;
    }
    
    @Impure
    protected @Nonnull Connection getConnection() throws DatabaseException, SQLException {
        if (connection == null) {
            connection = createConnection();
        }
        Ensure.that(connection != null).orThrow("The connection is null.");
        return connection;
    }
    
    @BeforeClass
    public static void setUp() throws DatabaseException, SQLException {
        final @Nullable String sqliteTestDirectoryProperty = System.getProperty("sqliteTestDirectory");
        Require.that(sqliteTestDirectoryProperty != null).orThrow("Failed to retrieve test directory for SQLite. Please specify it using the Java system property -DsqliteTestDirectory=...");
        
        final @Nonnull String sqliteTestDirectory = sqliteTestDirectoryProperty.endsWith(File.separator) ? sqliteTestDirectoryProperty : sqliteTestDirectoryProperty + File.separator;
        databaseFile = new File(sqliteTestDirectory + DB_NAME);
    }
    
    @AfterClass
    public static void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        if (databaseFile != null) {
//            databaseFile.delete();
        }
    }
    
    @Impure
    private void closeConnection() throws SQLException {
        connection.close();
        connection = null;
    }
    
    @Impure
    protected void assertTableExists(@Nonnull String table) throws SQLException, DatabaseException {
        try (@Nonnull ResultSet resultSet = getConnection().createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + table + "'")) {
            if (resultSet.next()) {
                assertThat(resultSet.getString(1)).isEqualToIgnoringCase(table);
            } else {
                fail("Failed to create table '" + table + "'.");
            }
        } finally {
            closeConnection();
        }
    }
    
}
