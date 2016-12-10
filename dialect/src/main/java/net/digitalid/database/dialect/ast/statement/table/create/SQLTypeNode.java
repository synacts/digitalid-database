package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.enumerations.SQLType;
import net.digitalid.database.subject.Site;

/**
 * This class enumerates the supported SQL types.
 */
@Immutable
public class SQLTypeNode implements SQLNode<SQLTypeNode> {
    
    private final @Nonnull SQLType sqlType;
    
    @Pure
    public @Nonnull SQLType getSQLType() {
        return sqlType;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLTypeNode(@Nonnull SQLType sqlType) {
        this.sqlType = sqlType;
    }
    
    @Pure
    public static @Nonnull SQLTypeNode of(@Nonnull SQLType sqlType) {
        return new SQLTypeNode(sqlType);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLTypeNode> transcriber = new Transcriber<SQLTypeNode>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLTypeNode type, @Nonnull Site site) throws InternalException {
            switch (type.sqlType) {
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
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLTypeNode> getTranscriber() {
        return transcriber;
    }
    
}
