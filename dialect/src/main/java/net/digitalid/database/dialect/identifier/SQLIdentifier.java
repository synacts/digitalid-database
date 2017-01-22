package net.digitalid.database.dialect.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.unit.Unit;

/**
 * This class represents an SQL identifier.
 * The subclasses increase the type safety.
 * 
 * @see SQLName
 * @see SQLAlias
 * @see SQLPrefix
 */
@Immutable
public interface SQLIdentifier extends SQLNode {
    
    /* -------------------------------------------------- String -------------------------------------------------- */
    
    /**
     * Returns this identifier as a string.
     */
    @Pure
    public @Nonnull @NonEmpty @MaxSize(63) String getString();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(Quotes.inDouble(getString()));
    }
    
}
