package net.digitalid.database.conversion.converter.iterable;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLArrayConverter extends SQLIterableConverter<Object[]> {
    
    @Override
    protected @Nonnull Collection<?> getCollection(@Nonnull Object object) {
        return Arrays.asList((Object[]) object);
    }
    
    @Override
    public @Nullable Object[] recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return recoverNullableObjectArray(result, annotations);
    }
    
}
