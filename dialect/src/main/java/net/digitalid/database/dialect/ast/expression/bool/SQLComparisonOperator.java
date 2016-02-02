package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryOperator;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.internal.UncoveredCaseException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class enumerates the supported comparison operators.
 */
@Immutable
public enum SQLComparisonOperator implements SQLBinaryOperator<SQLComparisonOperator> {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The {@code =} operator.
     */
    EQUAL(),
    
    /**
     * The {@code !=} operator.
     */
    UNEQUAL(),
    
    /**
     * The {@code >=} operator.
     */
    GREATER_OR_EQUAL(),
    
    /**
     * The {@code >} operator.
     */
    GREATER(),
    
    /**
     * The {@code <=} operator.
     */
    LESS_OR_EQUAL(),
    
    /**
     * The {@code <} operator.
     */
    LESS();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLComparisonOperator> transcriber = new Transcriber<SQLComparisonOperator>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLComparisonOperator node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        switch (node) {
            case EQUAL: string.append("="); break;
            case UNEQUAL: string.append("!="); break;
            case GREATER_OR_EQUAL: string.append(">="); break;
            case GREATER: string.append(">"); break;
            case LESS_OR_EQUAL: string.append("<="); break;
            case LESS: string.append("<"); break;
            default: throw UncoveredCaseException.with(node.name() + " not implemented.");
        }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLComparisonOperator> getTranscriber() {
        return transcriber;
    }
    
}
