package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class represents an SQL prefix.
 */
@Immutable
public final class SQLPrefix implements SQLIdentifier<SQLPrefix> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this SQL identifier.
     */
    private final @Nonnull @MaxSize(63) String value;
    
    /**
     * Returns the value of this SQL identifier.
     */
    @Pure
    public final @Nonnull @MaxSize(63) String getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL prefix with the given value.
     * 
     * @param value the value of the new SQL prefix.
     */
    protected SQLPrefix(@Nonnull @MaxSize(63) String value) {
        this.value = value;
    }
    
    /**
     * Returns a new SQL prefix with the given value.
     * 
     * @param value the value of the new SQL prefix.
     * 
     * @return a new SQL prefix with the given value.
     */
    @Pure
    public static @Nonnull SQLPrefix get(@Nonnull @MaxSize(63) String value) {
        return new SQLPrefix(value);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLPrefix> transcriber = new SQLIdentifierTranscriber<>();
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLPrefix> getTranscriber() {
        return transcriber;
    }
    
}
