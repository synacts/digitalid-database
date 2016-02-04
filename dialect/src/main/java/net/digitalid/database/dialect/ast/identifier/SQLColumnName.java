package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.size.MaxSize;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public class SQLColumnName<T extends SQLColumnName<T>> implements SQLIdentifier<T> {
    
    protected final @Nonnull String columnName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    protected SQLColumnName(@Nonnull String columnName) {
        this.columnName = columnName;
    }
    
    public static SQLColumnName get(@Nonnull String columnName) {
        return new SQLColumnName(columnName);
    }
    
    /* -------------------------------------------------- Identifier Value -------------------------------------------------- */
    
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        assert columnName.length() <= 63;
        return columnName;
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<? extends SQLColumnName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    @SuppressWarnings("unchecked")
    public @Nonnull Transcriber<T> getTranscriber() {
        return (Transcriber<T>) transcriber;
    }
    
}
