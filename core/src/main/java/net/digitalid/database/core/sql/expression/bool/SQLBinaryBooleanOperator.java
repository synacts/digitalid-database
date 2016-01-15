package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Immutable;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLBinaryOperator;
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
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
