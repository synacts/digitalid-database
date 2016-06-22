package net.digitalid.database.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import net.digitalid.utility.console.Console;
import net.digitalid.utility.directory.Directory;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Validated;

import net.digitalid.database.core.Database;
import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.operation.FailedOperationException;
import net.digitalid.database.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.interfaces.jdbc.JDBCDatabaseInstance;

/**
 * This class configures a MySQL database.
 */
@Immutable
public final class MySQLDatabaseInstance extends JDBCDatabaseInstance {
    
    /* -------------------------------------------------- Existence -------------------------------------------------- */
    
    /**
     * Returns whether a MySQL configuration exists.
     * 
     * @return whether a MySQL configuration exists.
     */
    @Pure
    public static boolean exists() {
        return new File(Directory.getDataDirectory().getPath() + File.separator + "MySQL.conf").exists();
    }
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    /**
     * Stores the server address of the database.
     */
    private final @Nonnull String server;
    
    /**
     * Stores the port number of the database.
     */
    private final @Nonnull String port;
    
    /**
     * Stores the name of the database.
     */
    private final @Nonnull String database;
    
    /**
     * Stores the user of the database.
     */
    private final @Nonnull String user;
    
    /**
     * Stores the password of the database.
     */
    private final @Nonnull String password;
    
    /**
     * Stores the user and the password as properties.
     */
    private final @Nonnull Properties properties = new Properties();
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Creates a new MySQL configuration by reading the properties from the indicated file or from the user's input.
     * 
     * @param name the name of the database configuration file (without the suffix).
     * @param reset whether the database is to be dropped first before creating it again.
     */
    @Committing
    private MySQLDatabaseInstance(@Nonnull @Validated String name, boolean reset) throws FailedUpdateExecutionException, IOException {
        super(new com.mysql.jdbc.Driver());
        
        Require.that(Configuration.isValidName(name)).orThrow("The name is valid for a database.");
        
        final @Nonnull File file = new File(Directory.getDataDirectory().getPath() + File.separator + name + ".conf");
        if (file.exists()) {
            try (@Nonnull FileInputStream stream = new FileInputStream(file); @Nonnull InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
                properties.load(reader);
                server = properties.getProperty("Server", "localhost");
                port = properties.getProperty("Port", "3306");
                database = properties.getProperty("Database", "digitalid");
                user = properties.getProperty("User", "root");
                password = properties.getProperty("Password", "");
            }
        } else {
            Console.write();
            Console.write("The MySQL database is not yet configured. Please provide the following information:");
            server = Console.readString("- Server (the default is \"localhost\"): ", "localhost");
            port = Console.readString("- Port (the default is 3306): ", "3306");
            database = Console.readString("- Database (the default is \"digitalid\"): ", "digitalid");
            user = Console.readString("- User (the default is \"root\"): ", "root");
            password = Console.readString("- Password (the default is empty): ", null);
            
            properties.setProperty("Server", server);
            properties.setProperty("Port", port);
            properties.setProperty("Database", database);
            properties.setProperty("User", user);
            properties.setProperty("Password", password);
            
            try (@Nonnull FileOutputStream stream = new FileOutputStream(file); @Nonnull OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8")) {
                properties.store(writer, "Configuration of the MySQL database");
            }
        }
        
        properties.clear();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        
        try (@Nonnull Connection connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port, properties); @Nonnull Statement statement = connection.createStatement()) {
            if (reset) { statement.executeUpdate("DROP DATABASE IF EXISTS " + database); }
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
    /**
     * Creates a new MySQL configuration by reading the properties from the indicated file or from the user's input.
     * 
     * @param name the name of the database configuration file (without the suffix).
     * @param reset whether the database is to be dropped first before creating it again.
     * 
     * @return a new MySQL configuration with the given name and potential reset.
     */
    @Pure
    @Committing
    public static @Nonnull MySQLDatabaseInstance get(@Nonnull @Validated String name, boolean reset) throws FailedUpdateExecutionException, IOException {
        return new MySQLDatabaseInstance(name, reset);
    }
    
    /**
     * Creates a new MySQL configuration by reading the properties from the indicated file or from the user's input.
     * 
     * @param name the name of the database configuration file (without the suffix).
     * 
     * @return a new MySQL configuration with the given name.
     */
    @Pure
    @Committing
    public static @Nonnull MySQLDatabaseInstance get(@Nonnull @Validated String name) throws FailedUpdateExecutionException, IOException {
        return new MySQLDatabaseInstance(name, false);
    }
    
    /**
     * Creates a new MySQL configuration by reading the properties from the default file or from the user's input.
     * 
     * @param reset whether the database is to be dropped first before creating it again.
     * 
     * @return a new MySQL configuration with the potential reset.
     */
    @Pure
    @Committing
    public static @Nonnull MySQLDatabaseInstance get(boolean reset) throws FailedUpdateExecutionException, IOException {
        return new MySQLDatabaseInstance("MySQL", reset);
    }
    
    /**
     * Creates a new MySQL configuration by reading the properties from the default file or from the user's input.
     * 
     * @return a new MySQL configuration.
     */
    @Pure
    @Committing
    public static @Nonnull MySQLDatabaseInstance get() throws FailedUpdateExecutionException, IOException {
        return new MySQLDatabaseInstance("MySQL", false);
    }
    
    /* -------------------------------------------------- Database -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String getURL() {
        return "jdbc:mysql://" + server + ":" + port + "/" + database + "?rewriteBatchedStatements=true";
    }
    
    @Pure
    @Override
    protected @Nonnull Properties getProperties() {
        return properties;
    }
    
    @Locked
    @Override
    @Committing
    public void dropDatabase() throws FailedOperationException {
        try (@Nonnull Statement statement = Database.createStatement()) {
            statement.executeUpdate("DROP DATABASE IF EXISTS " + database);
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
        Database.commit();
    }
    
    /* -------------------------------------------------- Syntax -------------------------------------------------- */
    
    /**
     * The pattern that valid database identifiers have to match.
     * Identifiers may in principle begin with a digit but unless quoted may not consist solely of digits.
     */
    private static final @Nonnull Pattern PATTERN = Pattern.compile("[a-z_][a-z0-9_$]*");
    
    @Pure
    @Override
    public boolean isValidIdentifier(@Nonnull String identifier) {
        return identifier.length() <= 64 && PATTERN.matcher(identifier).matches();
    }
    
    @Pure
    @Override
    public @Nonnull String PRIMARY_KEY() {
        return "BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY";
    }
    
    @Pure
    @Override
    public @Nonnull String TINYINT() {
        return "TINYINT";
    }
    
    @Pure
    @Override
    public @Nonnull String BINARY() {
        return "utf16_bin";
    }
    
    @Pure
    @Override
    public @Nonnull String NOCASE() {
        return "utf16_general_ci";
    }
    
    @Pure
    @Override
    public @Nonnull String CITEXT() {
        return "TEXT";
    }
    
    @Pure
    @Override
    public @Nonnull String BLOB() {
        return "LONGBLOB";
    }
    
    @Pure
    @Override
    public @Nonnull String HASH() {
        return "BINARY(33)";
    }
    
    @Pure
    @Override
    public @Nonnull String VECTOR() {
        return "BINARY(17)";
    }
    
    @Pure
    @Override
    public @Nonnull String FLOAT() {
        return "FLOAT";
    }
    
    @Pure
    @Override
    public @Nonnull String DOUBLE() {
        return "DOUBLE";
    }
    
    @Pure
    @Override
    public @Nonnull String REPLACE() {
        return "REPLACE";
    }
    
    @Pure
    @Override
    public @Nonnull String IGNORE() {
        return " IGNORE";
    }
    
    @Pure
    @Override
    public @Nonnull String GREATEST() {
        return "GREATEST";
    }
    
    @Pure
    @Override
    public @Nonnull String CURRENT_TIME() {
        return "UNIX_TIMESTAMP(SYSDATE()) * 1000 + MICROSECOND(SYSDATE(3)) DIV 1000";
    }
    
    @Pure
    @Override
    public @Nonnull String BOOLEAN(boolean value) {
        return Boolean.toString(value);
    }
    
    /* -------------------------------------------------- Index -------------------------------------------------- */
    
    /**
     * Returns the syntax for creating an index inside a table declaration.
     * 
     * @param columns the columns for which the index is to be created.
     * 
     * @return the syntax for creating an index inside a table declaration.
     * 
     * @require columns.length > 0 : "The columns are not empty.";
     */
    @Pure
    public @Nonnull String INDEX(@Nonnull String... columns) {
        Require.that(columns.length > 0).orThrow("The columns are not empty.");
        
        final @Nonnull StringBuilder string = new StringBuilder(", INDEX(");
        for (final @Nonnull String column : columns) {
            if (string.length() != 8) { string.append(", "); }
            string.append(column);
        }
        return string.append(")").toString();
    }
    
    /**
     * Creates an index outside a table declaration or does nothing.
     * 
     * @param statement the statement on which the creation is executed.
     * @param table the table on whose columns the index is to be created.
     * @param columns the columns for which the index is to be created.
     * 
     * @require columns.length > 0 : "The columns are not empty.";
     */
    @NonCommitting
    public void createIndex(@Nonnull Statement statement, @Nonnull String table, @Nonnull String... columns) {
        Require.that(columns.length > 0).orThrow("The columns are not empty.");
    }
    
}
