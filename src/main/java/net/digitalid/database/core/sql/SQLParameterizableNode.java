package net.digitalid.database.core.sql;

import javax.annotation.Nonnull;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.utility.annotations.reference.NonCapturable;

/**
 * This is the interface that all parameterizable SQL syntax tree nodes have to implement.
 */
public interface SQLParameterizableNode extends SQLNode {
    
    /**
     * Stores a value for each transcribed parameter (indicated by a question mark).
     * The number of stored values has to equal the number of transcribed parameters.
     * 
     * @param collector the value collector used to collect a value for each parameter.
     */
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException;
    
}
