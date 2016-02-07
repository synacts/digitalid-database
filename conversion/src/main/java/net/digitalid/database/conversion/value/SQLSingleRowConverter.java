package net.digitalid.database.conversion.value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public abstract class SQLSingleRowConverter<T> extends SQLConverter<T> {
    
    @Override 
    public SQLType getSQLType(Field field) {
        return null;
    }
    
    public abstract void collectValues(@Nullable Object object, Class<?> type, @NonCapturable @Nonnull SQLValues values) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException;
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        if (valuesList.isEmpty()) {
            valuesList.add(SQLValues.get());
        }
        for (@Nonnull @NullableElements SQLValues values : valuesList) {
            collectValues(object, type, values);
        }
    }
    
    @Override 
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        
    }
    
    @Override 
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        
    }
    
    @Override 
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
    
}
