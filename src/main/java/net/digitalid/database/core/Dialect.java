package net.digitalid.database.core;

import javax.annotation.Nonnull;
import net.digitalid.database.core.interfaces.Selection;
import net.digitalid.database.core.interfaces.jdbc.JDBCSelection;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;

/**
 * Description.
 */
@Immutable
public abstract class Dialect {
    
    /* -------------------------------------------------- Visits -------------------------------------------------- */
    
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLIdentifier identifier) {
        
    }
    
    /* -------------------------------------------------- Executions -------------------------------------------------- */
    
    public @Nonnull Selection execute(@Nonnull SQLSelectStatement statement) {
        return new JDBCSelection();
    }
    
}
