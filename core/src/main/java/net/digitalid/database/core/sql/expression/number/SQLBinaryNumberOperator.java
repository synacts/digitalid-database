package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLBinaryOperator;
import net.digitalid.database.core.table.Site;

/**
 * This class enumerates the supported binary number operators.
 */
@Immutable
public enum SQLBinaryNumberOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator represents addition.
     */
    ADDITION(),
    
    /**
     * This operator represents subtraction.
     */
    SUBTRACTION(),
    
    /**
     * This operator represents multiplication.
     */
    MULTIPLICATION(),
    
    /**
     * This operator represents division.
     */
    DIVISION(),
    
    /**
     * This operator represents integer division.
     */
    INTEGER_DIVISION(),
    
    /**
     * This operator represents modulo.
     */
    MODULO();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
