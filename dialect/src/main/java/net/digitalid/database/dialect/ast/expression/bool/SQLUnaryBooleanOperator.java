package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLUnaryOperator;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported unary boolean operators.
 */
@Immutable
public enum SQLUnaryBooleanOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator inverts the value.
     */
    NOT();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLUnaryBooleanOperator> transcriber = new Transcriber<SQLUnaryBooleanOperator>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLUnaryBooleanOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            switch (node) {
                case NOT: string.append("NOT"); break;
                default: throw InternalException.of(node.name() + " not implemented.");
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLUnaryBooleanOperator> getTranscriber() {
        return transcriber;
    }
    
}
