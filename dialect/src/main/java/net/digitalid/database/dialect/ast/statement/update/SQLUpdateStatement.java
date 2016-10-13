package net.digitalid.database.dialect.ast.statement.update;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;

import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;

/**
 * Description.
 */
public class SQLUpdateStatement implements SQLParameterizableNode {
    
    public SQLUpdateStatement() {
        
    }
    
    @Pure
    @Override
    public void storeValues(@Nonnull @NonCaptured @Modified SQLValueCollector collector) throws FailedSQLValueConversionException {
        
    }
    
    @Pure
    @Override
    public @Nonnull Transcriber getTranscriber() {
        return null;
    }
    
}
