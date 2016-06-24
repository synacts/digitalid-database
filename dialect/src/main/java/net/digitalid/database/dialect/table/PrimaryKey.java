package net.digitalid.database.dialect.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.types.CustomType;

/**
 *
 */
public class PrimaryKey {
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    private final @Nonnull CustomType type;
    
    @Pure
    public @Nonnull CustomType getType() {
        return type;
    }
    
    /* -------------------------------------------------- Column Name -------------------------------------------------- */
    
    private final @Nonnull String columnName;
    
    @Pure
    public @Nonnull String getColumnName() {
        return columnName;
    }
    
    /* -------------------------------------------------- Column Position -------------------------------------------------- */
    
    public final int columnPosition;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private PrimaryKey(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        this.type = type;
        this.columnName = columnName;
        this.columnPosition = columnPosition;
    }
    
    @Pure
    public static @Nonnull PrimaryKey with(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        return new PrimaryKey(type, columnName, columnPosition);
    }
    
}
