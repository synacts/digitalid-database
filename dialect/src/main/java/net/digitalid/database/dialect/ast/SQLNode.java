package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

/**
 * All SQL syntax tree nodes implement this interface.
 */
public interface SQLNode<N> {
    
    @Pure
    @Nonnull Transcriber<N> getTranscriber();
    
}
