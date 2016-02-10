package net.digitalid.database.conversion.value.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.exceptions.UnexpectedFailureException;
import net.digitalid.utility.property.ReadOnlyProperty;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLPropertyConverter extends SQLConverter<ReadOnlyProperty<?, ?>> {
    
    private @Nonnull Method getGetMethod(@Nonnull Class<?> propertyClass) {
        try {
            final @Nonnull Method method = propertyClass.getMethod("get");
            return method;
        } catch (@Nonnull NoSuchMethodException exception) {
            throw UnexpectedFailureException.with(exception);
        }
    }
    
    private @Nonnull Class<?> getPropertyType(@Nonnull Class<?> propertyClass) {
        @Nonnull Method method = getGetMethod(propertyClass);
        final @Nonnull Class<?> returnType = method.getReturnType();
        return returnType;
    }
    
    @Override
    public @Nullable SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
        final @Nonnull Class<?> propertyType = getPropertyType(type);
        return SQL.FORMAT.getSQLConverter(propertyType).getSQLType(propertyType, annotations);
    }
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLValues> values, @Nonnull @NonNullableElements Annotation[] annotations) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException {
        final @Nonnull Method getMethod = getGetMethod(type);
        final @Nonnull Class<?> returnType = getMethod.getReturnType();
        final @Nullable Object propertyObject;
        if (object != null) {
            try {
                propertyObject = getMethod.invoke(object);
            } catch (@Nonnull IllegalAccessException | InvocationTargetException exception) {
                throw UnexpectedFailureException.with(exception);
            }
        } else {
            propertyObject = null;
        }
        SQL.FORMAT.getSQLConverter(returnType).collectValues(propertyObject, returnType, values, null);
    }
    
    @Override
    public void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(columnName, tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    // TODO: move to "SimpleTypeConverter"
    @Override
    public void putColumnDeclarations(@Nonnull Class<?> type, String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nullable Site site, @Nonnull @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(columnName), getSQLType(type, annotations), SQLColumnDefinition.of(annotations), SQLColumnConstraint.of(annotations, columnName));
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException {
        // TODO: if this is an extensible property, we should probably insert into the collection table.
    }
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        // TODO: if this is an extensible property, we should probably create the collection table.
    }
    
    @Override
    public void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        
    }
    
    @Override
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        final @Nonnull Class<?> propertyType = getPropertyType(type);
        final @Nullable Object recoveredObject = SQL.FORMAT.getConverter(propertyType).recoverNullable(propertyType, result);
    
        if (recoveredObject != null) {
            // TODO: what if the property constructor is not empty?
            try {
                final @Nonnull Object property = type.newInstance();
                final @Nonnull Method setter = type.getMethod("set");
                setter.invoke(property, recoveredObject);
                return property;
            } catch (@Nonnull NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException exception) {
                throw UnexpectedFailureException.with("Failed to reconstruct property:", exception);
            }
        } else {
            return null;
        }
    }
    
}
