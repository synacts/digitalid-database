package net.digitalid.database.dialect.ast.statement.table.create;

import java.sql.Types;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.fixes.Quotes;
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
    
    private final @Nonnull CustomType customType;
    
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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLType type, @Nonnull Site site, @Nonnull @NonCaptured StringBuilder string, boolean parameterizable) throws InternalException {
            switch (type) {
                case EMPTY: string.append("BOOLEAN"); break;
                case BOOLEAN: string.append("BOOLEAN"); break;
                case INTEGER08: string.append("TINYINT"); break;
                case INTEGER16: string.append("SMALLINT"); break;
                case INTEGER32: string.append("INT"); break;
                case INTEGER64: string.append("BIGINT"); break;
                case INTEGER: string.append("BLOB"); break;
                case DECIMAL32: string.append("FLOAT"); break;
                case DECIMAL64: string.append("DOUBLE"); break;
                case STRING01: string.append("CHAR(1)"); break;
                case STRING64: string.append("VARCHAR(64) COLLATE utf16_bin"); break;
                case STRING: string.append("TEXT"); break;
                case BINARY128: string.append("BINARY(16)"); break;
                case BINARY256: string.append("BINARY(32)"); break;
                case BINARY: string.append("MEDIUMBLOB"); break;
                default: throw UnexpectedValueException.with("type", type);
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLType> getTranscriber() {
        return transcriber;
    }
    
}
