package net.digitalid.database.conversion.value.iterable;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
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
public class SQLCollectionsConverter extends SQLConverter<Collection<?>> {
    
    @Override public SQLType getSQLType(Field field) {
        return null;
    }
    
    @Override 
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        // TODO: assert that object is of type collection
        final @Nullable Collection<?> collection = (Collection<?>) object;
    
        final @Nonnull Class<?> genericType = Integer.class;// TODO: getGenericTypeOfCollection();
        if (collection != null) {
            FreezableArrayList<SQLValues> newList = FreezableArrayList.get();
            for (@Nullable Object element : collection) {
                final @Nonnull FreezableArrayList<SQLValues> copyOfValuesList = FreezableArrayList.get();
                copyOfValuesList.addAll(valuesList);
                @Nonnull @NonNullableElements FreezableArrayList<SQLValues> newValuesList = FreezableArrayList.get();
                SQL.FORMAT.getConverter(genericType).collectValues(element, genericType, copyOfValuesList);
                newList.addAll(copyOfValuesList);
            }
            valuesList.clear();
            valuesList.addAll(newList);
        } else {
            SQL.FORMAT.getConverter(genericType).collectValues(null, genericType, valuesList);
        }
    }
    
    @Override 
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull Class<?> genericType = Integer.class;// TODO: getGenericTypeOfCollection();
        SQL.FORMAT.getConverter(genericType).putColumnNames(field, qualifiedColumnNames);
    }
    
    @Override 
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: assert that field is of type collections.
        final @Nonnull Class<?> genericType = Integer.class;// TODO: getGenericTypeOfCollection();
        SQL.FORMAT.getConverter(genericType).putColumnDeclarations(field, columnDeclarations);
    }
    
    @Override 
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
    
}
