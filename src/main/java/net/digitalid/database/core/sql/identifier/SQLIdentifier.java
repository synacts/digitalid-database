package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.collections.annotations.size.SizeAtMost;
import net.digitalid.utility.system.exceptions.internal.InternalException;

/**
 * This class represents an SQL identifier.
 * The subclasses increase the type safety.
 * 
 * @see SQLName
 * @see SQLAlias
 * @see SQLPrefix
 */
@Immutable
public abstract class SQLIdentifier implements SQLNode {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Stores the value of this SQL identifier.
     */
    private final @Nonnull @SizeAtMost(63) String value;
    
    /**
     * Returns the value of this SQL identifier.
     * 
     * @return the value of this SQL identifier.
     */
    @Pure
    public final @Nonnull @SizeAtMost(63) String getValue() {
        return value;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL identifier with the given value.
     * 
     * @param value the value of the new SQL identifier.
     */
    protected SQLIdentifier(@Nonnull @SizeAtMost(63) String value) {
        assert value.length() <= 63 : "The length of the value is at most 63.";
        
        this.value = value;
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
