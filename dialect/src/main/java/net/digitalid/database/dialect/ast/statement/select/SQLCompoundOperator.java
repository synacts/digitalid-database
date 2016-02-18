package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This SQL node represents an SQL compound operator used in an SQL select statement.
 */
public enum SQLCompoundOperator implements SQLNode<SQLCompoundOperator> {
    
    UNION,
    INTERSECT,
    EXCEPT;
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLCompoundOperator> transcriber = new Transcriber<SQLCompoundOperator>() {
    
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLCompoundOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            string.append(node.name());
        }
    };
    
    @Override 
    public @Nonnull Transcriber<SQLCompoundOperator> getTranscriber() {
        return transcriber;
    }
    
}
