package net.digitalid.database.dialect.ast.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedValueException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLVariadicOperator;

/**
 * This class enumerates the supported variadic string nodes.
 */
@Immutable
public enum SQLVariadicStringOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This node concatenates the strings.
     */
    CONCAT(),
    
    /**
     * This node returns the greatest string.
     */
    GREATEST(),
    
    /**
     * This node returns the first non-null string.
     */
    COALESCE();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLVariadicStringOperator> transcriber = new Transcriber<SQLVariadicStringOperator>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicStringOperator node, @Nonnull Site site)  throws InternalException {
            switch (node) {
                case CONCAT:
                    return "CONCAT";
                case GREATEST:
                    return "GREATEST";
                case COALESCE:
                    return "COALESCE";
                default:
                    throw UnexpectedValueException.with("node", node);
            }
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLVariadicStringOperator> getTranscriber() {
        return transcriber;
    }
    
}
