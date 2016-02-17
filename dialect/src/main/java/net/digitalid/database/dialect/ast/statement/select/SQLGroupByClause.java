package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 *
 */
public class SQLGroupByClause implements SQLParameterizableNode<SQLGroupByClause> {
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        
    }
    
    @Override
    public @Nonnull Transcriber<SQLGroupByClause> getTranscriber() {
        return null;
    }
    
}
