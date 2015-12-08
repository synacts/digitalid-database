package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.size.SizeAtMost;

/**
 * This class represents an SQL prefix.
 */
@Immutable
public final class SQLPrefix extends SQLIdentifier {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL prefix with the given value.
     * 
     * @param value the value of the new SQL prefix.
     */
    protected SQLPrefix(@Nonnull @SizeAtMost(63) String value) {
        super(value);
    }
    
    /**
     * Returns a new SQL prefix with the given value.
     * 
     * @param value the value of the new SQL prefix.
     * 
     * @return a new SQL prefix with the given value.
     */
    @Pure
    public static @Nonnull SQLPrefix get(@Nonnull @SizeAtMost(63) String value) {
        return new SQLPrefix(value);
    }
    
}
