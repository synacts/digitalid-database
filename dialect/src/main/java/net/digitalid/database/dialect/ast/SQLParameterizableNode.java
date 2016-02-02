package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;

/**
 * This is the abstract class that all parameterizable SQL syntax tree nodes have to implement.
 */
public interface SQLParameterizableNode<T> extends SQLNode<T> {
    
    /**
     * Stores a value for each transcribed parameter (indicated by a question mark).
     * The number of stored values has to equal the number of transcribed parameters.
     * 
     * @param collector the value collector used to collect a value for each parameter.
     */
    public abstract void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException;
    
}
