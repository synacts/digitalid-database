package net.digitalid.database.testing.h2;

import java.util.Properties;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.jdbc.JDBCDatabase;

import org.h2.Driver;

/**
 * This class implements an H2 database.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class H2JDBCDatabase extends JDBCDatabase {
    
    protected H2JDBCDatabase() {
        super(new Driver());
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
