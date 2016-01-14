package net.digitalid.database.core.sql.statement.select;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.statement.insert.SQLValuesOrStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * Description.
 */
public class SQLSelectStatement implements SQLParameterizableNode, SQLValuesOrStatement {
    
    public SQLSelectStatement() {
        
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        dialect.transcribe(site, string, this);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        // TODO
    }
    
}
