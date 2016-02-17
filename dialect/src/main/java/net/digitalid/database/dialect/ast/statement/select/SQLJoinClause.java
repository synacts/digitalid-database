package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public class SQLJoinClause implements SQLNode<SQLJoinClause> {
    
    @Override
    public @Nonnull Transcriber<SQLJoinClause> getTranscriber() {
        return null;
    }
}
