package net.digitalid.database.dialect.ast.statement.update;

import javax.annotation.Nonnull;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * Description.
 */
public class SQLUpdateStatement implements SQLParameterizableNode {
    
    public SQLUpdateStatement() {
        
    }
    
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        
    }
    
    @Nonnull
    @Override
    public Transcriber getTranscriber() {
        return null;
    }
}
