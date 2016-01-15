package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.annotations.size.MaxSize;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

/**
 * This class represents an SQL alias.
 */
@Immutable
public final class SQLAlias extends SQLIdentifier {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL alias with the given value.
     * 
     * @param value the value of the new SQL alias.
     */
    protected SQLAlias(@Nonnull @MaxSize(63) String value) {
        super(value);
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
    
}
