package net.digitalid.database.core.declaration;

import java.sql.Types;
import javax.annotation.Nonnull;
import net.digitalid.database.core.configuration.Database;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class enumerates the various SQL types.
 */
@Immutable
public enum SQLType {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The SQL type for big integers.
     */
    BIGINT(Types.BIGINT, "BIGINT"),
    
    /**
     * The SQL type for normal integers.
     */
    INT(Types.INTEGER, "INT"),
    
    /**
     * The SQL type for small integers.
     */
    SMALLINT(Types.SMALLINT, "SMALLINT"),
    
    /**
     * The SQL type for tiny integers.
     */
    TINYINT(Types.TINYINT, Database.getConfiguration().TINYINT()),
    
    /**
     * The SQL type for floats.
     */
    FLOAT(Types.FLOAT, Database.getConfiguration().FLOAT()),
    
    /**
     * The SQL type for doubles.
     */
    DOUBLE(Types.DOUBLE, Database.getConfiguration().DOUBLE()),
    
    /**
     * The SQL type for hashes.
     */
    HASH(Types.BINARY, Database.getConfiguration().HASH()),
    
    /**
     * The SQL type for vectors.
     */
    VECTOR(Types.BINARY, Database.getConfiguration().VECTOR()),
    
    /**
     * The SQL type for booleans.
     */
    BOOLEAN(Types.TINYINT, "BOOLEAN"),
    
    /**
     * The SQL type for strings.
     */
    STRING(Types.VARCHAR, "TEXT"),
    
    /**
     * The SQL type for varchars.
     */
    VARCHAR(Types.VARCHAR, "VARCHAR(63) COLLATE " + Database.getConfiguration().BINARY()),
    
    /**
     * The SQL type for chars.
     */
    CHAR(Types.VARCHAR, "CHAR(1)"),
    
    /**
     * The SQL type for large objects.
     */
    BLOB(Types.BLOB, Database.getConfiguration().BLOB());
    
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
    public int getCode() {
        return code;
    }
    
    /* -------------------------------------------------- String -------------------------------------------------- */
    
    /**
     * Stores the string of this SQL type.
     */
    private final @Nonnull String string;
    
    @Pure
    @Override
    public @Nonnull String toString() {
        return string;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL type with the given code and string.
     * 
     * @param code the JDBC code of this SQL type.
     * @param string the string of this SQL type.
     */
    private SQLType(int code, @Nonnull String string) {
        this.code = code;
        this.string = string;
    }
    
}
