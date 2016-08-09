package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class represents an SQL alias.
 */
@Immutable
public interface SQLAlias<T extends SQLAlias<T>> extends SQLIdentifier<T> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value of this SQL identifier.
     */
    @Pure
    public @Nonnull @MaxSize(63) String getValue();
    
 
}
