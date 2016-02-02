package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.method.Pure;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class represents an SQL name.
 */
@Immutable
public final class SQLName implements SQLIdentifier<SQLName> {
    
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
     * Creates a new SQL name with the given value.
     * 
     * @param value the value of the new SQL name.
     */
    protected SQLName(@Nonnull @MaxSize(63) String value) {
        this.value = value;
    }
    
    /**
     * Returns a new SQL name with the given value.
     * 
     * @param value the value of the new SQL name.
     * 
     * @return a new SQL name with the given value.
     */
    @Pure
    public static @Nonnull SQLName get(@Nonnull @MaxSize(63) String value) {
        return new SQLName(value);
    }
    
    /* -------------------------------------------------- Prefixing -------------------------------------------------- */
    
    /**
     * Returns this name prefixed with the given prefix.
     * 
     * @param prefix the prefix to be prepended to this name.
     * 
     * @return this name prefixed with the given prefix.
     */
    @Pure
    public final @Nonnull SQLName prefixedWith(@Nonnull SQLPrefix prefix) {
        assert prefix.getValue().length() + getValue().length() <= 62 : "The added lengths of the prefix and this name may be at most 62.";
        
        return new SQLName(prefix.getValue() + "_" + this.getValue());
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    public @Nonnull Transcriber<SQLName> getTranscriber() {
        return transcriber;
    }
    
}
