package net.digitalid.database.testing.h2;

import java.util.Properties;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.jdbc.JDBCDatabaseInstance;

import org.h2.Driver;

/**
 *
 */
public class H2JDBCDatabaseInstance extends JDBCDatabaseInstance {
    
    final @Nonnull String url;
    
    private H2JDBCDatabaseInstance(@Nonnull String url) {
        super(new Driver());
        this.url = url;
    }
    
    @Pure
    public static H2JDBCDatabaseInstance get(@Nonnull String url) {
        return new H2JDBCDatabaseInstance(url);
    }
    
    @Pure
    @Override
    protected @Nonnull String getURL() {
        return url;
    }
    
    @Pure
    @Override
    protected @Nonnull Properties getProperties() {
        final @Nonnull Properties properties = new Properties();
        properties.setProperty("user", "sa");
        properties.setProperty("password", "sa");
        return properties;
    }
    
}
