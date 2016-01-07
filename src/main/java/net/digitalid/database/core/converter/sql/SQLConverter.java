package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.conversion.Converter;

/**
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    protected void convertNullable(@Nullable Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull ValueCollector valueCollector) throws FailedValueStoringException {
       if (object == null) {
            valueCollector.setEmpty();
        } else {
            convertNonNullable(object, type, valueCollector);
        }
    }
    
    protected abstract void convertNonNullable(@Nonnull Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull ValueCollector valueCollector) throws FailedValueStoringException;
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    protected @Nonnull Object recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException {
        @Nullable Object object = recoverNullable(type, result);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    protected abstract @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException;
    
}
