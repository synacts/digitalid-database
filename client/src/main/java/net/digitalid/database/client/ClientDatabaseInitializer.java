package net.digitalid.database.client;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.file.Files;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.jdbc.JDBCDatabaseBuilder;

import org.sqlite.JDBC;

/**
 * This class initializes the database for the client.
 */
@Utility
public abstract class ClientDatabaseInitializer {
    
    /**
     * Initializes the database.
     */
    @PureWithSideEffects
    @Initialize(target = Database.class, dependencies = SQLDialect.class)
    public static void initializeDatabase() throws SQLException {
        final @Nonnull String name = "Client";
        final @Nonnull String URL = "jdbc:sqlite:" + Files.relativeToConfigurationDirectory("data/" + name + ".db");
        Database.instance.set(JDBCDatabaseBuilder.withDriver(new JDBC()).withURL(URL).build());
    }
    
}
