package net.digitalid.database.dialect.ast;


import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.configuration.Configuration;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.Site;

/**
 * A dialect implements a particular version of the structured filter language (SQL). This abstract class transcribes SQL nodes to SQL statements
 * by calling the default transcriber of the node. Subclasses of SQLDialect may override the behavior by calling their own transcribers before falling back to the default transcriber.
 */
@Immutable
public abstract class SQLDialect {
    
    public static final @Nonnull Configuration<SQLDialect> dialect = Configuration.withUnknownProvider();
    //private static @Nullable SQLDialect instance;
    
    /**
     * Transcribes an SQL node by calling the default transcriber of the node which stores the SQL statement as a string in the string builder.
     */
    @Pure
    public @Nonnull String transcribe(@Nonnull Site site, @Nonnull SQLNode<?> node) throws InternalException {
        return node.getTranscriber().transcribeNode(this, node, site);
    }
    
    // TODO: Should throw InitializationError instead.
   /* public static @Nonnull SQLDialect getDialect() throws InternalException {
        if (instance == null) {
            synchronized (SQLDialect.class) {
                final @Nonnull ServiceLoader<SQLDialectServiceProviderInterface> serviceLoader = ServiceLoader.load(SQLDialectServiceProviderInterface.class);
                final @Nonnull Iterator<SQLDialectServiceProviderInterface> iterator = serviceLoader.iterator();
                if (iterator.hasNext()) {
                    final @Nonnull SQLDialectServiceProviderInterface serviceProviderInterface = iterator.next();
                    instance = serviceProviderInterface.getSQLDialect();
                } else {
                    throw InitializationException.with("Failed to initialize SQL dialect.");
                }
            }
        }
        return instance;
    }*/
    
    @Pure
    public static @Nonnull SQLDialect getDialect() {
        return dialect.get();
    }
    
}
