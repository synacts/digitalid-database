package net.digitalid.database.server;

import java.sql.SQLException;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.interfaces.Database;

/**
 * This class initializes the database for the server.
 */
@Utility
public abstract class ServerDatabaseInitializer {
    
    /**
     * Initializes the database.
     */
    @PureWithSideEffects
    @Initialize(target = Database.class, dependencies = SQLDialect.class)
    @TODO(task = "Adapt the code in this method.", date = "2017-03-05", author = Author.KASPAR_ETTER)
    public static void initializeDatabase() throws SQLException {
//        final @Nonnull File file = new File(Directory.getDataDirectory().getPath() + "/" + name + ".conf");
//        if (file.exists()) {
//            try (@Nonnull FileInputStream stream = new FileInputStream(file); @Nonnull InputStreamReader reader = new InputStreamReader(stream, "UTF-8")) {
//                properties.load(reader);
//                server = properties.getProperty("Server", "localhost");
//                port = properties.getProperty("Port", "3306");
//                database = properties.getProperty("Database", "digitalid");
//                user = properties.getProperty("User", "root");
//                password = properties.getProperty("Password", "");
//            }
//        } else {
//            Console.write();
//            Console.write("The MySQL database is not yet configured. Please provide the following information:");
//            server = Console.readString("- Server (the default is \"localhost\"): ", "localhost");
//            port = Console.readString("- Port (the default is 3306): ", "3306");
//            database = Console.readString("- Database (the default is \"digitalid\"): ", "digitalid");
//            user = Console.readString("- User (the default is \"root\"): ", "root");
//            password = Console.readString("- Password (the default is empty): ", null);
//            
//            properties.setProperty("Server", server);
//            properties.setProperty("Port", port);
//            properties.setProperty("Database", database);
//            properties.setProperty("User", user);
//            properties.setProperty("Password", password);
//            
//            try (@Nonnull FileOutputStream stream = new FileOutputStream(file); @Nonnull OutputStreamWriter writer = new OutputStreamWriter(stream, "UTF-8")) {
//                properties.store(writer, "Configuration of the MySQL database");
//            }
//        }
//        
//        properties.clear();
//        properties.setProperty("user", user);
//        properties.setProperty("password", password);
//        
//        try (@Nonnull Connection connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port, properties); @Nonnull Statement statement = connection.createStatement()) {
//            if (reset) { statement.executeUpdate("DROP DATABASE IF EXISTS " + database); }
//            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database);
//        } catch (@Nonnull SQLException exception) {
//            throw FailedUpdateExecutionException.get(exception);
//        }
//        
//        final @Nonnull String URL = "jdbc:mysql://" + server + ":" + port + "/" + database + "?rewriteBatchedStatements=true";
//        Database.instance.set(JDBCDatabaseBuilder.withDriver(new Driver()).withURL(URL).withUser("sa").withPassword("sa").build());
    }
    
}
