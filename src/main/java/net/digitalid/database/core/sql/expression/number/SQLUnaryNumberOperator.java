package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLUnaryOperator;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * This class enumerates the supported unary number operators.
 */
@Immutable
public enum SQLUnaryNumberOperator implements SQLUnaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This operator inverts the value.
     */
    INVERT(),
    
    /**
     * This operator returns the absolute value.
     */
    ABS(),
    
    /**
     * This operator returns the sign of the value.
     */
    SIGN(),
    
    /**
     * This operator rounds the value.
     */
    ROUND(),
    
    /**
     * This operator rounds the value down.
     */
    FLOOR(),
    
    /**
     * This operator rounds the value up.
     */
    CEIL(),
    
    /**
     * This operator returns the square root of the value.
     */
    SQRT();
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
