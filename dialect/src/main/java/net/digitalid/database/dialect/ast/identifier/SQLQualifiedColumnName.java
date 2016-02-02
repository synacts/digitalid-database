package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.validation.annotations.size.MaxSize;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public class SQLQualifiedColumnName implements SQLIdentifier<SQLQualifiedColumnName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    public final @Nonnull String columnName;

    public final @Nullable String tableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected SQLQualifiedColumnName(@Nonnull String columnName, @Nullable String tableName) {
        this.columnName = columnName;
        this.tableName = tableName;
    }
    
    public static SQLQualifiedColumnName get(@Nonnull String columnName) {
        return new SQLQualifiedColumnName(columnName, null);
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
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLQualifiedColumnName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    public @Nonnull
    Transcriber<SQLQualifiedColumnName> getTranscriber() {
        return transcriber;
    }
}
