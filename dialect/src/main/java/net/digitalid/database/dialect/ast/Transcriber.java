package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.table.Site;

/**
 *
 */
public abstract class Transcriber<N> {
    
    public final @Nonnull Class<N> type;
    
    @SuppressWarnings("unchecked")
    protected Transcriber() {
        this.type = (Class<N>) this.getClass().getEnclosingClass();
    }
    
    protected Transcriber(@Nonnull Class<N> type) {
        this.type = type;
    }
    
    protected abstract @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull N node, @Nonnull Site site) throws InternalException;
    
    public @Nonnull String transcribeNode(@Nonnull SQLDialect dialect, @Nonnull SQLNode node, @Nonnull Site site) throws InternalException {
        if (type.isInstance(node)) {
            return transcribe(dialect, type.cast(node), site);
        } else {
            throw new IllegalArgumentException("Cannot transcribe node of type '" + node.getClass() + "' with transcriber for type '" + type + "'.");
        }
    }
    
}
