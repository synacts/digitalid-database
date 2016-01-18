package net.digitalid.database.core.converter.sql;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.annotations.size.MinSize;
import net.digitalid.utility.conversion.Convertible;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.validation.reference.NonCapturable;

import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;

/**
 *
 */
public class SQLConvertibleConverter<C extends Convertible> extends SQLConverter<C> {
    
    @Override
    protected void convertNonNullable(@Nonnull Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull SQLInsertStatement sqlInsertStatement) throws FailedValueStoringException, ConverterNotFoundException, StoringException {
        final @Nonnull @MinSize(1) Field[] fields = type.getFields();
        if (fields.length == 1) {
            final @Nonnull Field field = fields[0];
            final @Nonnull SQLConverter<?> converter = SQL.FORMAT.getConverter(field);
            final @Nullable Object fieldValue;
            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw StoringException.get(type, e);
            }
            converter.convertNullable(fieldValue, field.getType(), sqlInsertStatement);
        } else {
            
        }
    }

    @Nullable
    @Override
    protected Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        return null;
    }
}
