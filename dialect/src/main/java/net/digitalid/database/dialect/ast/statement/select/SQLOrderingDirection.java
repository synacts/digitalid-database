package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This node represents the ordering direction: ascending or descending.
 */
public enum SQLOrderingDirection implements SQLNode<SQLOrderingDirection> {
    
    ASC,
    DESC;
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLOrderingDirection> transcriber = new Transcriber<SQLOrderingDirection>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLOrderingDirection node, @Nonnull Site site)  throws InternalException {
            string.append(node.name());
        }
        
    };
    
    @Override 
    public @Nonnull Transcriber<SQLOrderingDirection> getTranscriber() {
        return transcriber;
    }
    
}
