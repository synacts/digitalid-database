package net.digitalid.database.dialect.spi;

import net.digitalid.database.dialect.ast.SQLDialect;

/**
 *
 */
public interface SQLDialectServiceProviderInterface {
    
    public SQLDialect getSQLDialect();
    
}
