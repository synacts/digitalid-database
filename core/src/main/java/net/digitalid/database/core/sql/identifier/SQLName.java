package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.size.MaxSize;

/**
 * This class represents an SQL name.
 */
@Immutable
public final class SQLName extends SQLIdentifier {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL name with the given value.
     * 
     * @param value the value of the new SQL name.
     */
    protected SQLName(@Nonnull @MaxSize(63) String value) {
        super(value);
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
    
}
