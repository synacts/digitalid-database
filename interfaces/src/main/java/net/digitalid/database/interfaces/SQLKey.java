package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.model.CustomType;

/**
 *
 */
public class SQLKey {
    
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
    
    private SQLKey(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        this.type = type;
        this.columnName = columnName;
        this.columnPosition = columnPosition;
    }
    
    @Pure
    public static @Nonnull SQLKey with(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        return new SQLKey(type, columnName, columnPosition);
    }
    
}
