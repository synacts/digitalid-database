package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.interfaces.Site;

/**
 * Transcribes an SQL AST node into a string for a certain dialect.
 */
public abstract class Transcriber<N> {
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    /**
     * The type for the given transcriber, which is used to check if the correct node is transcribed.
     */
    public final @Nonnull Class<N> type;
    
    /* -------------------------------------------------- Constructors -------------------------------------------------- */
    
    /**
     * Creates a new transcriber and uses the enclosing class as the type for the transcriber.
     */
    @SuppressWarnings("unchecked")
    protected Transcriber() {
        this.type = (Class<N>) this.getClass().getEnclosingClass();
    }
    
    /**
     * Creates a new transcriber and uses the given class as the type for the transcriber.
     */
    protected Transcriber(@Nonnull Class<N> type) {
        this.type = type;
    }
    
    /* -------------------------------------------------- Transcribe -------------------------------------------------- */
    
    /**
     * Transcribes a node with the given dialect for a given site into an SQL string.
     */
    @Pure
    protected abstract @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull N node, @Nonnull Site site) throws InternalException;
    
    /**
     * Transcribes a node with the given dialect for a given site into an SQL string. Before the transcription, it is verified that the transcribed can be used to transcribe the given node.
     */
    @Pure
    public @Nonnull String transcribeNode(@Nonnull SQLDialect dialect, @Nonnull SQLNode node, @Nonnull Site site) throws InternalException {
        if (type.isInstance(node)) {
            return transcribe(dialect, type.cast(node), site);
        } else {
            throw new IllegalArgumentException("Cannot transcribe node of type '" + node.getClass() + "' with transcriber for type '" + type + "'.");
        }
    }
    
}
