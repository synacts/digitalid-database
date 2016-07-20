package net.digitalid.database.core;

import java.sql.Types;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.conversion.converter.types.CustomType;

/**
 *
 */
public enum SQLType {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The SQL type for empty.
     */
    EMPTY(Types.BOOLEAN, null),
    
    /**
     * The SQL type for booleans.
     */
    BOOLEAN(Types.BOOLEAN, CustomType.BOOLEAN),
    
    /**
     * The SQL type for tiny integers.
     */
    INTEGER08(Types.TINYINT, CustomType.INTEGER08),
    
    /**
     * The SQL type for small integers.
     */
    INTEGER16(Types.SMALLINT, CustomType.INTEGER16),
    
    /**
     * The SQL type for normal integers.
     */
    INTEGER32(Types.INTEGER, CustomType.INTEGER32),
    
    /**
     * The SQL type for big integers.
     */
    INTEGER64(Types.BIGINT, CustomType.INTEGER64),
    
    /**
     * The SQL type for big integers.
     */
    INTEGER(Types.BLOB, CustomType.INTEGER),
    
    /**
     * The SQL type for floats.
     */
    DECIMAL32(Types.FLOAT, CustomType.DECIMAL32),
    
    /**
     * The SQL type for doubles.
     */
    DECIMAL64(Types.DOUBLE, CustomType.DECIMAL64),
    
    /**
     * The SQL type for chars.
     */
    STRING01(Types.CHAR, CustomType.STRING01),
    
    /**
     * The SQL type for varchars.
     */
    STRING64(Types.VARCHAR, CustomType.STRING64),
    
    /**
     * The SQL type for strings.
     */
    STRING(Types.VARCHAR, CustomType.STRING),
    
    /**
     * The SQL type for vectors.
     */
    BINARY128(Types.BINARY, CustomType.BINARY128),
    
    /**
     * The SQL type for hashes.
     */
    BINARY256(Types.BINARY, CustomType.BINARY256),
    
    /**
     * The SQL type for large objects.
     */
    BINARY(Types.BLOB, CustomType.BINARY);
    
    /* -------------------------------------------------- Code -------------------------------------------------- */
    
    /**
     * Stores the JDBC code of this SQL type.
     */
    private final int code;
    
    /**
     * Returns the JDBC code of this SQL type.
     * 
     * @return the JDBC code of this SQL type.
     */
    @Pure
    public int getCode() {
        return code;
    }
    
    /* -------------------------------------------------- Custom Type -------------------------------------------------- */
    
    /**
     * The custom type that corresponds to the SQL type.
     */
    private final @Nonnull CustomType customType;
    
    /**
     * Returns the custom type.
     */
    @Pure
    public @Nonnull CustomType getCustomType() {
        return customType;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL type with the given code.
     * 
     * @param code the JDBC code of this SQL type.
     */
    private SQLType(int code, @Nonnull CustomType customType) {
        this.code = code;
        this.customType = customType;
    }
    
    /**
     * Returns an SQL type for a given Digital ID custom type, or throws an exception
     * if the type cannot be mapped.
     */
    public static @Nonnull SQLType of(@Nonnull CustomType type) {
        for (@Nonnull SQLType sqlType : values()) {
            if (sqlType.customType == type) {
                return sqlType;
            }
        }
        throw new UnsupportedOperationException("SQL type can only be a primitive type, but Digital ID custom type was " + Quotes.inSingle(type));
    }
    
    /**
     * Returns an SQL type for a given Digital ID custom type, or throws an exception
     * if the type cannot be mapped.
     */
    public static @Nonnull SQLType of(@Nonnull int code) {
        for (@Nonnull SQLType sqlType : values()) {
            if (sqlType.code == code) {
                return sqlType;
            }
        }
        throw new UnsupportedOperationException("SQL type does not support the type code " + Quotes.inSingle(code));
    }
    
}
