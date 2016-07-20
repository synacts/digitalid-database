package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class represents a column name in an SQL statement.
 */
// TODO: check if it's really a good idea to have a generic parameter here. Seems to be cumbersome to require a wildcard everywhere.
public class SQLColumnName<T extends SQLColumnName<T>> implements SQLIdentifier<T> {
    
    /**
     * The name of the column.
     */
    protected final @Nonnull String columnName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Create a new SQL column name instance with a given column name string.
     */
    protected SQLColumnName(@Nonnull String columnName) {
        this.columnName = columnName;
    }
    
    /**
     * Returns an SQL column name instance for a given column name string.
     */
    @Pure
    public static @Nonnull SQLColumnName<?> get(@Nonnull String columnName) {
        return new SQLColumnName(columnName);
    }
    
    /* -------------------------------------------------- Identifier Value -------------------------------------------------- */
    
    /**
     * Returns the value of the column name.
     */
    @Pure
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        assert columnName.length() <= 63;
        return columnName;
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber for the SQL node.
     */
    private static final @Nonnull Transcriber<? extends SQLColumnName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Pure
    @Override
    @SuppressWarnings("unchecked")
    public @Nonnull Transcriber<T> getTranscriber() {
        return (Transcriber<T>) transcriber;
    }
    
}
