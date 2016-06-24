package net.digitalid.database.dialect.ast.statement.table.create;

import java.sql.Types;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class enumerates the supported SQL types.
 */
@Immutable
public enum SQLType implements SQLNode {
    
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
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLType> transcriber = new Transcriber<SQLType>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLType type, @Nonnull Site site) throws InternalException {
            switch (type) {
                case EMPTY: return "BOOLEAN";
                case BOOLEAN: return "BOOLEAN";
                case INTEGER08: return "TINYINT";
                case INTEGER16: return "SMALLINT";
                case INTEGER32: return "INT";
                case INTEGER64: return "BIGINT";
                case INTEGER: return "BLOB";
                case DECIMAL32: return "FLOAT";
                case DECIMAL64: return "DOUBLE";
                case STRING01: return "CHAR(1)";
                case STRING64: return "VARCHAR(64) COLLATE utf16_bin";
                case STRING: return "TEXT";
                case BINARY128: return "BINARY(16)";
                case BINARY256: return "BINARY(32)";
                case BINARY: return "MEDIUMBLOB";
                default: throw UnexpectedValueException.with("type", type);
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLType> getTranscriber() {
        return transcriber;
    }
    
}
