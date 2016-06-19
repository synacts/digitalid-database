package net.digitalid.database.dialect.ast.statement.delete;

import javax.annotation.Nonnull;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * Description.
 */
public class SQLDeleteStatement implements SQLParameterizableNode {
    
    public SQLDeleteStatement() {
        
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
