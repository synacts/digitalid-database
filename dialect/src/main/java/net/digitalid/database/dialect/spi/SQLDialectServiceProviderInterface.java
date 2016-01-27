package net.digitalid.database.dialect.spi;

import net.digitalid.database.dialect.SQLDialect;

/**
 *
 */
public interface SQLDialectServiceProviderInterface {
    
    public SQLDialect getSQLDialect();
    
}
