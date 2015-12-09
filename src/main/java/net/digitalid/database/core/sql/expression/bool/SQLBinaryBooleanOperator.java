package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLBinaryOperator;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class enumerates the supported binary boolean operators.
 */
@Immutable
public enum SQLBinaryBooleanOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * The {@code AND} operator.
     */
    AND(),
    
    /**
     * The {@code OR} operator.
     */
    OR(),
    
    /**
     * The {@code XOR} operator.
     */
    XOR(),
    
    /**
     * The {@code =} operator.
     */
    EQUAL(),
    
    /**
     * The {@code !=} operator.
     */
    UNEQUAL();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
