package net.digitalid.database.dialect.ast;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;

import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;

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
    @Pure
    public abstract void storeValues(@Nonnull @NonCaptured @Modified SQLValueCollector collector) throws FailedSQLValueConversionException;
    
}
