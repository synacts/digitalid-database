package net.digitalid.database.dialect;

import java.util.Iterator;
import java.util.ServiceLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.internal.InitializationException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.spi.SQLDialectServiceProviderInterface;

/**
 * A dialect implements a particular version of the structured query language (SQL). This abstract class transcribes SQL nodes to SQL statements
 * by calling the default transcriber of the node. Subclasses of SQLDialect may override the behaviour by calling their own transcribers before falling back to the default transcriber.
 */
@Immutable
public abstract class SQLDialect {
    
    private static @Nullable SQLDialect instance;
    /**
     * Transcribes an SQL node by calling the default transcriber of the node which stores the SQL statement as a string in the string builder.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLNode<?> node) throws InternalException {
        node.getTranscriber().transcribeNode(this, node, site, string);
    }
    
    // TODO: Should throw InitializationError instead.
    public static @Nonnull SQLDialect getDialect() throws InternalException {
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
    }
    
}
