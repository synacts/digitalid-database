package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLIdentifier;

/**
 *
 */
public class H2SQLIdentifierTranscriber<T extends SQLIdentifier<T>> extends Transcriber<T> {
    
    H2SQLIdentifierTranscriber(@Nonnull Class<T> type) {
        super(type);
    }
    
    @Override
    protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull T node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
        string.append(node.getValue().toUpperCase());
    }
    
}
