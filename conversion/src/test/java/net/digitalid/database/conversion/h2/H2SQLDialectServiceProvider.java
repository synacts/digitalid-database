package net.digitalid.database.conversion.h2;

import javax.annotation.Nonnull;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.spi.SQLDialectServiceProviderInterface;

/**
 *
 */
public class H2SQLDialectServiceProvider implements SQLDialectServiceProviderInterface {
    
    @Override
    public @Nonnull
    SQLDialect getSQLDialect() {
        return new H2Dialect();
    }
    
}
