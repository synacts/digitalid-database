package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.conversion.Converter;

/**
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    protected void convertNullable(@Nullable Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull SQLInsertStatement sqlInsertStatement) throws FailedValueStoringException {
       if (object == null) {
           // TODO: what about prefixes?!
           // TODO: move to SQLNullConverter
           sqlInsertStatement.setColumnName(type.getSimpleName());
           sqlInsertStatement.setColumnValue(null);
        } else {
            convertNonNullable(object, type, sqlInsertStatement);
        }
    }
    
    protected abstract void convertNonNullable(@Nonnull Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull SQLInsertStatement sqlInsertStatement) throws FailedValueStoringException;
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    protected @Nonnull Object recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        @Nullable Object object = recoverNullable(type, result);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    protected abstract @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException;
    
}
