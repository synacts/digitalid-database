package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * Description.
 */
public class SQLCreateTableStatement implements SQLNode<SQLCreateTableStatement> {
    
    public SQLCreateTableStatement() {
        
    }
    
    public String toSQL(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        getTranscriber().transcribeNode(dialect, this, site, stringBuilder);
        return stringBuilder.toString();
    }
    
    @Nonnull
    @Override
    public Transcriber<SQLCreateTableStatement> getTranscriber() {
        // TODO: implement!
        return null;
    }
}
