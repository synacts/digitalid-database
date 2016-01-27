package net.digitalid.database.dialect.ast.statement.update;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * Description.
 */
public class SQLUpdateStatement implements SQLParameterizableNode {
    
    public SQLUpdateStatement() {
        
    }
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        
    }
    
    @Nonnull
    @Override
    public Transcriber getTranscriber() {
        return null;
    }
}
