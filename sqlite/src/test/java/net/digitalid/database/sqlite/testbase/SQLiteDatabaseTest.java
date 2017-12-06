/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import net.digitalid.utility.contracts.Ensure;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.testing.UtilityTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;

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
    protected @Nonnull Connection createConnection() throws SQLException {
        final @Nonnull Connection connection;
        Require.that(databaseFile != null).orThrow("The database file was never created.");
        
        connection = DriverManager.getConnection(DB_CONNECTION_URL_PREFIX + databaseFile.getAbsolutePath());
        // SQLite only supports SERIALIZABLE and READ_UNCOMMITTED. For testing purposes, it does not really matter which isolation level we are using. In production, the isolation level should probably be set to SERIALIZABLE to ensure that multiple threads with transactions to the database can never read uncommitted changes.
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        connection.setAutoCommit(true);
        
        Ensure.that(!connection.isClosed()).orThrow("The database connection should be open.");
        Ensure.that(!connection.isReadOnly()).orThrow("The database connection should be writable.");
        
        connection.createStatement().execute("ATTACH '" + databaseFile.getAbsolutePath() + "' AS " + SCHEMA_NAME);
        connection.setAutoCommit(false);
        Ensure.that(databaseFile.exists()).orThrow("Failed to create the database file $", databaseFile.getAbsolutePath());
        return connection;
    }
    
    @Impure
    protected @Nonnull Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = createConnection();
        }
        Ensure.that(connection != null).orThrow("The connection is null.");
        return connection;
    }
    
    @BeforeClass
    public static void setUp() throws SQLException {
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
    protected void assertTableExists(@Nonnull String table) throws SQLException {
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
