package net.digitalid.database.dialect.table;

import javax.annotation.Nonnull;

/**
 *
 */
public class PrimaryKey {
    
    private final @Nonnull Class<?> type;
    private final @Nonnull String columnName;
    public final int columnPosition;
    
    private PrimaryKey(@Nonnull Class<?> type, @Nonnull String columnName, int columnPosition) {
        this.type = type;
        this.columnName = columnName;
        this.columnPosition = columnPosition;
    }
    
    public static @Nonnull PrimaryKey with(@Nonnull Class<?> type, @Nonnull String columnName, int columnPosition) {
        return new PrimaryKey(type, columnName, columnPosition);
    }
    
    public @Nonnull Class<?> getType() {
        return type;
    }
    
    public @Nonnull String getColumnName() {
        return columnName;
    }
    
}
