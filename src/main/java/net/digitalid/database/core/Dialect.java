package net.digitalid.database.core;

import javax.annotation.Nonnull;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.jdbc.JDBCSelectionResult;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * Description.
 */
@Immutable
public abstract class Dialect {
    
    /* -------------------------------------------------- Visits -------------------------------------------------- */
    
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLIdentifier identifier) throws InternalException {
        
    }
    
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLType type) throws InternalException {
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
            default: throw InternalException.get(type + " not implemented.");
        }
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    public @Nonnull SelectionResult execute(@Nonnull Site site, @Nonnull SQLSelectStatement statement) throws InternalException {
        return new JDBCSelectionResult();
    }
    
}
