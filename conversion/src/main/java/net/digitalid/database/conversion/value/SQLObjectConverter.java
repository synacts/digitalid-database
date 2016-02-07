package net.digitalid.database.conversion.value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.reflection.ReflectionUtility;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.conversion.exceptions.ConformityViolation;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.annotations.References;
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
public class SQLObjectConverter<T extends Convertible> extends SQLConverter<T> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public @Nonnull SQLType getSQLType(Field field) {
        throw new UnsupportedOperationException("Only simple types can be converted to SQL types.");
    }
    
    /* -------------------------------------------------- Value Collection -------------------------------------------------- */
    
    /**
     * Returns the object that is referenced by a field annotated with the @References annotation. If the parent object
     * is null, null is returned, otherwise the value of the field is returned.
     */
    private @Nullable Object retrieveReferencedObject(@Nonnull Field referencee, @Nullable Object parentObject) throws NoSuchFieldException, ConformityViolation {
        final @Nullable Object referencedObject;
        if (parentObject != null) {
            try {
                referencee.setAccessible(true);
                referencedObject = referencee.get(parentObject);
            } catch (IllegalAccessException e) {
                throw ConformityViolation.with("The referenced field '" + referencee.getName() + "' is not accessible.", e);
            }
        } else {
            referencedObject = null;
        }
        return referencedObject;
    }
    
    /**
     * Returns the field referenced through the referencee field, which is the field annotated with {@link References @References}, by using the columnName declared in the
     * {@link References @References} annotation as the field name.
     */
    private @Nonnull Field retrieveReferencedField(@Nonnull Field field) throws NoSuchFieldException, InternalException {
        final @Nonnull References references = field.getAnnotation(References.class);
        final @Nonnull Field referencedField = field.getType().getField(references.columnName());
        return referencedField;
    }
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLValues> values, Annotation[] annotations) throws FailedValueStoringException, InternalException, StoringException, StructureException, NoSuchFieldException {
        assert object == null || object instanceof Convertible: "The object is of type Convertible.";
        
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field field : fields) {
            final @Nullable Object valueSource;
            final @Nonnull Field storableField;
            if (field.isAnnotationPresent(References.class)) {
                valueSource = retrieveReferencedObject(field, object);
                storableField = retrieveReferencedField(field);
            } else {
                valueSource = object;
                storableField = field;
            }
            final @Nonnull SQLConverter<?> converter = SQL.FORMAT.getConverter(storableField);
            final @Nullable Object referencedFieldValue;
            if (valueSource != null) {
                try {
                    storableField.setAccessible(true);
                    referencedFieldValue = storableField.get(valueSource);
                } catch (IllegalAccessException e) {
                    final @Nonnull Class<?> storableType;
                    if (field.isAnnotationPresent(References.class)) {
                        storableType = field.getType();
                    } else {
                        storableType = type;
                    }
                    throw ConformityViolation.with("The field '" + storableField.getName() + "' of type '" + storableType + "' is not accessible.", e);
                }
            } else {
                referencedFieldValue = null;
            }
            converter.collectValues(referencedFieldValue, storableField.getType(), values, null);
        }
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    private void putColumnNamesOfEmbeddedType(@Nonnull Class<?> type, @Nonnull String tableName, @NonCapturable @Nonnull @NonNullableElements FreezableList<? super SQLQualifiedColumnName> columnNames) throws ConverterNotFoundException, StructureException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnNames(embeddedField, tableName, columnNames);
        }
    }
    
    @Override
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull @NonNullableElements FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull Class<?> type = field.getType();
        if (field.isAnnotationPresent(References.class)) {
            final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
            for (@Nonnull Field embeddedField : fields) {
                final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
                sqlConverter.putColumnNames(embeddedField, tableName, qualifiedColumnNames);
            }
        } else {
            putColumnNamesOfEmbeddedType(type, tableName, qualifiedColumnNames);
        }
    }
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    private void putColumnDeclarationsOfEmbeddedType(@Nonnull Class<?> type, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnDeclarations(embeddedField, columnDeclarations);
        }
    }
    
    private void putColumnDeclarationsOfReferencedType(@Nonnull Field field, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws NoSuchFieldException, ConverterNotFoundException {
        final @Nonnull References references = field.getAnnotation(References.class);
        final @Nonnull Field referencedField = field.getType().getField(references.columnName());
        
        final @Nonnull SQLConverter<?> referencedFieldConverter = SQL.FORMAT.getConverter(referencedField);
        final @Nonnull SQLType sqlType = referencedFieldConverter.getSQLType(referencedField);
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get(field.getName()), sqlType, SQLColumnDefinition.of(field), SQLColumnConstraint.of(field));
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        Require.that(Convertible.class.isAssignableFrom(field.getType())).orThrow("The field has the type 'Convertible'");
        
        if (field.isAnnotationPresent(References.class)) {
            putColumnDeclarationsOfReferencedType(field, columnDeclarations);
        } else {
            final @Nonnull Class<?> type = field.getType();
            putColumnDeclarationsOfEmbeddedType(type, columnDeclarations);
        }
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @Override
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        final @Nonnull @NullableElements FreezableArrayList<Object> recoveredValues = FreezableArrayList.get();
        for (@Nonnull Field field : fields) {
            final @Nonnull SQLConverter<?> converter = SQL.FORMAT.getConverter(field);
            final @Nullable Object fieldValue = converter.recoverNullable(type, result);
            recoveredValues.add(fieldValue);
        }
        return Converter.recoverNonNullableObject(type, recoveredValues);
    }
    
}
