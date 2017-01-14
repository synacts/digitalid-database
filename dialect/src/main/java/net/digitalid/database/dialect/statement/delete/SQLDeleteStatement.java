package net.digitalid.database.dialect.statement.delete;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;

import net.digitalid.database.dialect.SQLParameterizableNode;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLEncoder;

/**
 * TODO: Implement
 */
public class SQLDeleteStatement implements SQLParameterizableNode {
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLEncoder collector) throws FailedSQLValueConversionException {
        
    }
    
    @Pure
    @Override
    public @Nonnull Transcriber getTranscriber() {
        return null;
    }
    
}
