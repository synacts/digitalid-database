package net.digitalid.database.core.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;

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
        super(name, SQLType.INTEGER64, null);
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
        return getName(prefix) + Database.getConfiguration().PRIMARY_KEY();
    }
    
}
