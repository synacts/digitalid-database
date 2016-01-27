package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.annotations.size.MaxSize;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class represents an SQL alias.
 */
@Immutable
public final class SQLAlias extends SQLIdentifier<SQLAlias> {
    
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
     * Creates a new SQL alias with the given value.
     * 
     * @param value the value of the new SQL alias.
     */
    protected SQLAlias(@Nonnull @MaxSize(63) String value) {
        this.value = value;
    }
    
    /**
     * Returns a new SQL alias with the given value.
     * 
     * @param value the value of the new SQL alias.
     * 
     * @return a new SQL alias with the given value.
     */
    @Pure
    public static @Nonnull SQLAlias get(@Nonnull @MaxSize(63) String value) {
        return new SQLAlias(value);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLAlias> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    public @Nonnull Transcriber<SQLAlias> getTranscriber() {
        return transcriber;
    }
    
}
