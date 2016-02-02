package net.digitalid.database.dialect.ast.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.internal.UncoveredCaseException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLVariadicOperator;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported variadic number operators.
 */
@Immutable
public enum SQLVariadicNumberOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the greatest number.
     */
    GREATEST(),
    
    /**
     * This operator returns the first non-null number.
     */
    COALESCE();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLVariadicNumberOperator> transcriber = new Transcriber<SQLVariadicNumberOperator>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLVariadicNumberOperator operator, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            switch (operator) {
                case GREATEST: string.append("GREATEST"); break;
                case COALESCE: string.append("COALESCE"); break;
                default: throw UncoveredCaseException.with(operator.name() + " not implemented.");
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLVariadicNumberOperator> getTranscriber() {
        return transcriber;
    }
    
}
