package net.digitalid.database.core.sql.expression.bool;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * This class enumerates the supported comparison operators.
 */
@Immutable
public enum SQLComparisonOperator implements SQLNode {
    
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
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
}
