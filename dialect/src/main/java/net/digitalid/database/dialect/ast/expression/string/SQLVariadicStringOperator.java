package net.digitalid.database.dialect.ast.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
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
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicStringOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            switch (node) {
                case CONCAT:
                    string.append("CONCAT");
                    break;
                case GREATEST:
                    string.append("GREATEST");
                    break;
                case COALESCE:
                    string.append("COALESCE");
                    break;
                default:
                    throw InternalException.of(node.name() + " not implemented.");
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLVariadicStringOperator> getTranscriber() {
        return transcriber;
    }
    
}
