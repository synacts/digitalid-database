package net.digitalid.database.dialect.statement.select.unordered.simple.sources;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.identifier.table.SQLTableAlias;

/**
 * The source of an SQL select statement.
 * 
 * @see SQLJoinSource
 * @see SQLTableSource
 * @see SQLSelectSource
 */
@Immutable
public interface SQLSource<SOURCE extends SQLNode> extends SQLNode {
    
    /* -------------------------------------------------- Source -------------------------------------------------- */
    
    /**
     * Returns the source.
     */
    @Pure
    public @Nonnull SOURCE getSource();
    
    /* -------------------------------------------------- Alias -------------------------------------------------- */
    
    /**
     * Returns the alias of this source.
     */
    @Pure
    public @Nullable SQLTableAlias getAlias();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("(");
        dialect.unparse(getSource(), unit, string);
        string.append(")");
        final @Nullable SQLTableAlias alias = getAlias();
        if (alias != null) {
            string.append(" AS ");
            dialect.unparse(alias, unit, string);
        }
    }
    
}
