package net.digitalid.database.conversion.value.integer;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.value.SQLSingleRowConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLInteger32Converter extends SQLSingleRowConverter<Integer> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public @Nullable SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
        return SQLType.INTEGER32;
    }
    
    /* -------------------------------------------------- Value Collection -------------------------------------------------- */
    
    @Override
    public void collectValues(@Nullable Object object, Class<?> type, @NonCapturable @Nonnull SQLValues values) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException {
        assert object == null || object instanceof Integer: "The object is of type integer32.";
        
        // TODO: handle null-cases properly
        final @Nonnull SQLNumberLiteral numberLiteral = SQLNumberLiteral.get((Integer) object);
        values.addValue(numberLiteral);
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @Override
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        final @Nullable Integer value = result.getInteger32();
        return value;
    }
    
}
