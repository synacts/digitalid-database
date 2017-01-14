package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.subject.site.Site;

/**
 *
 */
public class H2SQLIdentifierTranscriber<T extends SQLIdentifier<T>> extends Transcriber<T> {
    
    H2SQLIdentifierTranscriber(@Nonnull Class<T> type) {
        super(type);
    }
    
    @Pure
    @Override
    protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull T node, @Nonnull Site site) throws InternalException {
        return node.getValue().toUpperCase();
    }
    
}
