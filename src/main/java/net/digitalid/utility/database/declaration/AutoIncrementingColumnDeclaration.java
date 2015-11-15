package net.digitalid.utility.database.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.database.configuration.Database;

/**
 * This class implements an auto-incrementing column declaration.
 */
@Immutable
public final class AutoIncrementingColumnDeclaration extends ColumnDeclaration {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new auto-incrementing column declaration with the given name.
     * 
     * @param name the name of the new auto-incrementing column declaration.
     */
    protected AutoIncrementingColumnDeclaration(@Nonnull @Validated String name) {
        super(name, SQLType.BIGINT, null);
    }
    
    /**
     * Returns a new auto-incrementing column declaration with the given name.
     * 
     * @param name the name of the new auto-incrementing column declaration.
     * 
     * @return a new auto-incrementing column declaration with the given name.
     */
    @Pure
    public static @Nonnull AutoIncrementingColumnDeclaration get(@Nonnull @Validated String name) {
        return new AutoIncrementingColumnDeclaration(name);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix) {
        assert prefix == null || isValidPrefix(prefix) : "The prefix is null or valid.";
        
        return (prefix == null ? "" : prefix + "_") + getName() + Database.getConfiguration().PRIMARY_KEY();
    }
    
}
