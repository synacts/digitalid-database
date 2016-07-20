package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.spi.SQLDialectServiceProviderInterface;

/**
 *
 */
public class H2SQLDialectServiceProvider implements SQLDialectServiceProviderInterface {
    
    @Pure
    @Override
    public @Nonnull
    SQLDialect getSQLDialect() {
        return new H2Dialect();
    }
    
}
