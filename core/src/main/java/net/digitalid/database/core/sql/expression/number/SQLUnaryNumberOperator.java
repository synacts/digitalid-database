package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLUnaryOperator;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported unary number operators.
 */
@Immutable
public enum SQLUnaryNumberOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator returns the rounded value.
     */
    ROUND(),
    
    /**
     * This operator returns the negated value.
     */
    NEGATE(),
    
    /**
     * This operator returns the absolute value.
     */
    ABSOLUTE();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
