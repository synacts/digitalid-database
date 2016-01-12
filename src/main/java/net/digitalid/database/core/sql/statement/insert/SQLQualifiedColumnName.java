package net.digitalid.database.core.sql.statement.insert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.utility.collections.annotations.size.MaxSize;

/**
 *
 */
public class SQLQualifiedColumnName extends SQLIdentifier<SQLQualifiedColumnName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    public final @Nonnull String columnName;

    public final @Nullable String tableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLQualifiedColumnName(@Nonnull String columnName, @Nullable String tableName) {
        this.columnName = columnName;
        this.tableName = tableName;
    }
    
    public static SQLQualifiedColumnName get(@Nonnull String columnName, @Nullable String tableName) {
        return new SQLQualifiedColumnName(columnName, tableName);
    }
    
    /* -------------------------------------------------- Identifier Value -------------------------------------------------- */
    
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        String qualifiedColumnName = (tableName ==null ? "" : tableName + ".") + columnName;
        assert qualifiedColumnName.length() <= 63;
        return qualifiedColumnName;
    }
    
}
