package net.digitalid.database.conversion.value.property;

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
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
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
    
    private @Nonnull Class<?> getPropertyType(@Nonnull Field field) {
        final @Nonnull Class<?> propertyClass = field.getType();
        return getPropertyType(propertyClass);
    }
    
    @Override
    public SQLType getSQLType(@Nullable Field field) {
        if (field == null) {
            throw new UnsupportedOperationException("SQL type of property can only be determined if the field is given.");
        }
        return SQL.FORMAT.getConverter(getPropertyType(field)).getSQLType(null);
    }
    
    @Override
    public void collectValues(@Nullable Object object, Class<?> type, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLValues> values) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException {
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
        SQL.FORMAT.getConverter(returnType).collectValues(propertyObject, returnType, values);
    }
    
    // TODO: move to "SimpleTypeConverter"
    @Override
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    // TODO: move to "SimpleTypeConverter"
    @Override
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(field.getName()), getSQLType(field), SQLColumnDefinition.of(field), SQLColumnConstraint.of(field));
        columnDeclarations.add(columnDeclaration);
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
