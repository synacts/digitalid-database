package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.internal.UncoveredCaseException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryOperator;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported binary boolean operators.
 */
@Immutable
public enum SQLBinaryBooleanOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The logical conjunction.
     */
    AND(),
    
    /**
     * The inclusive disjunction.
     */
    OR(),
    
    /**
     * The exclusive disjunction.
     */
    XOR(),
    
    /**
     * The logical equality.
     */
    EQUAL(),
    
    /**
     * The logical inequality.
     * The same as {@link #XOR}.
     */
    UNEQUAL();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLBinaryBooleanOperator> transcriber = new Transcriber<SQLBinaryBooleanOperator>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLBinaryBooleanOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
        switch (node) {
            case AND: string.append("AND"); break;
            case OR: string.append("OR"); break;
            case XOR: string.append("XOR"); break;
            case EQUAL: string.append("="); break;
            case UNEQUAL: string.append("!="); break;
            default: throw UncoveredCaseException.with(node.name() + " not implemented.");
        }           
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLBinaryBooleanOperator> getTranscriber() {
        return transcriber;
    }
    
}
