package net.digitalid.database.dialect.spi;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.dialect.ast.SQLDialect;

/**
 *
 */
public interface SQLDialectServiceProviderInterface {
    
    @Pure
    public SQLDialect getSQLDialect();
    
}
