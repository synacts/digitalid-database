package net.digitalid.database.core.sql.statement.table.create;

import java.sql.Types;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported SQL types.
 */
@Immutable
public enum SQLType implements SQLNode {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The SQL type for empty.
     */
    EMPTY(Types.BOOLEAN),
    
    /**
     * The SQL type for booleans.
     */
    BOOLEAN(Types.BOOLEAN),
    
    /**
     * The SQL type for tiny integers.
     */
    INTEGER08(Types.TINYINT),
    
    /**
     * The SQL type for small integers.
     */
    INTEGER16(Types.SMALLINT),
    
    /**
     * The SQL type for normal integers.
     */
    INTEGER32(Types.INTEGER),
    
    /**
     * The SQL type for big integers.
     */
    INTEGER64(Types.BIGINT),
    
    /**
     * The SQL type for big integers.
     */
    INTEGER(Types.BLOB),
    
    /**
     * The SQL type for floats.
     */
    DECIMAL32(Types.FLOAT),
    
    /**
     * The SQL type for doubles.
     */
    DECIMAL64(Types.DOUBLE),
    
    /**
     * The SQL type for chars.
     */
    STRING01(Types.CHAR),
    
    /**
     * The SQL type for varchars.
     */
    STRING64(Types.VARCHAR),
    
    /**
     * The SQL type for strings.
     */
    STRING(Types.VARCHAR),
    
    /**
     * The SQL type for vectors.
     */
    BINARY128(Types.BINARY),
    
    /**
     * The SQL type for hashes.
     */
    BINARY256(Types.BINARY),
    
    /**
     * The SQL type for large objects.
     */
    BINARY(Types.BLOB);
    
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
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL type with the given code.
     * 
     * @param code the JDBC code of this SQL type.
     */
    private SQLType(int code) {
        this.code = code;
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
