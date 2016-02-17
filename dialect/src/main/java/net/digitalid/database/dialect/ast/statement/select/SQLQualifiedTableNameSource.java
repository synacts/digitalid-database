package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 *
 */
public class SQLQualifiedTableNameSource implements SQLSource<SQLQualifiedTableNameSource> {
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLQualifiedTableNameSource(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nullable String alias) {
        this.qualifiedTableName = qualifiedTableName;
        this.alias = alias;
    }
    
    @Override
    public @Nonnull Transcriber<SQLQualifiedTableNameSource> getTranscriber() {
        return null;
    }
    
}
