package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public class SQLSelectStatementSource implements SQLSource<SQLSelectStatementSource> {
    
    @Override
    public @Nonnull Transcriber<SQLSelectStatementSource> getTranscriber() {
        return null;
    }
    
}
