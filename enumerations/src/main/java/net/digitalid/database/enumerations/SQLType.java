package net.digitalid.database.enumerations;

import java.sql.Types;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.exceptions.CaseException;
import net.digitalid.utility.exceptions.CaseExceptionBuilder;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class enumerates the various types that are supported.
 */
@Immutable
@Deprecated // TODO: Do we really want to replicate the type enumeration? A custom type can also be mapped with a map to the corresponding JDBC code.
public enum SQLType {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
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
    
    private final int code;
    
    /**
     * Returns the JDBC code of this SQL type.
     */
    @Pure
    public int getCode() {
        return code;
    }
    
    /* -------------------------------------------------- Custom Type -------------------------------------------------- */
    
    private final @Nonnull CustomType customType;
    
    /**
     * Returns the custom type that corresponds to the SQL type.
     */
    @Pure
    public @Nonnull CustomType getCustomType() {
        return customType;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL type with the given JDBC code and custom type.
     */
    private SQLType(int code, @Nonnull CustomType customType) {
        this.code = code;
        this.customType = customType;
    }
    
    /**
     * Returns an SQL type for a given Digital ID custom type or throws a {@link CaseException} if the type cannot be mapped.
     */
    @Pure
    public static @Nonnull SQLType of(@Nonnull CustomType type) {
        for (@Nonnull SQLType sqlType : values()) {
            if (sqlType.customType == type) {
                return sqlType;
            }
        }
        throw CaseExceptionBuilder.withVariable("type").withValue(type).build();
    }
    
    /**
     * Returns an SQL type for a given Digital ID custom type, or throws a {@link CaseException} if the type cannot be mapped.
     */
    @Pure
    public static @Nonnull SQLType of(int code) {
        for (@Nonnull SQLType sqlType : values()) {
            if (sqlType.code == code) {
                return sqlType;
            }
        }
        throw CaseExceptionBuilder.withVariable("code").withValue(code).build();
    }
    
}
