package net.digitalid.database.dialect.ast.statement.delete;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * TODO: Implement
 */
public class SQLDeleteStatement implements SQLParameterizableNode {
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        
    }
    
    @Pure
    @Override
    public @Nonnull Transcriber getTranscriber() {
        return null;
    }
    
}
