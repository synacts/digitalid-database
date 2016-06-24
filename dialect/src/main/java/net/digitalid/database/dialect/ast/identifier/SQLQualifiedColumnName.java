package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;

/**
 *
 */
public class SQLQualifiedColumnName extends SQLColumnName<SQLQualifiedColumnName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    public final @Nullable String tableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected SQLQualifiedColumnName(@Nonnull String columnName, @Nullable String tableName) {
        super(columnName);
        this.tableName = tableName;
    }
    
    @Pure
    public static SQLQualifiedColumnName get(@Nonnull String columnName, @Nullable String tableName) {
        return new SQLQualifiedColumnName(columnName, tableName);
    }
    
    /* -------------------------------------------------- Identifier Value -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        String qualifiedColumnName = (tableName ==null ? "" : tableName + ".") + columnName;
        assert qualifiedColumnName.length() <= 63;
        return qualifiedColumnName;
    }
    
}
