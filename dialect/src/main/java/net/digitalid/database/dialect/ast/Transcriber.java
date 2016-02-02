package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;

/**
 *
 */
public abstract class Transcriber<N> {
    
    public final @Nonnull Class<N> type;
    
    @SuppressWarnings("unchecked")
    protected Transcriber() {
        this.type = (Class<N>) this.getClass();
    }
    
    protected Transcriber(@Nonnull Class<N> type) {
        this.type = type;
    }
    
    protected abstract void transcribe(@Nonnull SQLDialect dialect, @Nonnull N node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException;
    
    public void transcribeNode(@Nonnull SQLDialect dialect, @Nonnull SQLNode node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        if (type.isInstance(node)) {
            transcribe(dialect, type.cast(node), site, string);
        } else {
            throw new IllegalArgumentException("Cannot transcribe node of type '" + node.getClass() + "' with transcriber for type '" + type + "'.");
        }
    }
    
}
