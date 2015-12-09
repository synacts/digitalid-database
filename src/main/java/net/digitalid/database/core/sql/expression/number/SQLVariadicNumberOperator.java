package net.digitalid.database.core.sql.expression.number;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.expression.SQLVariadicOperator;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.system.exceptions.InternalException;

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
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
