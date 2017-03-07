package net.digitalid.database.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.console.Console;
import net.digitalid.utility.file.Files;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.interfaces.Database;
import net.digitalid.database.jdbc.JDBCDatabaseBuilder;

import com.mysql.jdbc.Driver;

/**
 * This class initializes the database for the server.
 */
@Utility
public abstract class ServerDatabaseInitializer {
    
    /**
     * Initializes the database.
     */
    @PureWithSideEffects
    @Initialize(target = Database.class, dependencies = Files.class)
    public static void initializeDatabase() throws SQLException, IOException {
        final @Nonnull Properties properties = new Properties();
        final @Nonnull String server, port, database, user, password;
        final @Nonnull File file = Files.relativeToConfigurationDirectory("MySQL.conf");
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
            Console.writeLine();
            Console.writeLine("The MySQL database is not yet configured. Please provide the following information:");
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
        
        final @Nonnull String URL = "jdbc:mysql://" + server + ":" + port + "/" + database + "?rewriteBatchedStatements=true";
        Database.instance.set(JDBCDatabaseBuilder.withDriver(new Driver()).withURL(URL).withUser(user).withPassword(password).build());
    }
    
}
