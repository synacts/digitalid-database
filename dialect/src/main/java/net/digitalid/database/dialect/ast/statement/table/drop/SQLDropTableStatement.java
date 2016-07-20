package net.digitalid.database.dialect.ast.statement.table.drop;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * Description.
 */
public class SQLDropTableStatement implements SQLNode {
    
    public SQLDropTableStatement() {
        
    }
    
    @Pure
    @Nonnull
    @Override
    public Transcriber getTranscriber() {
        return null;
    }
    
}
