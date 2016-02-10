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
import net.digitalid.database.core.Database;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLObjectConverter<T extends Convertible> extends SQLConverter<T> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public @Nullable SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
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
    
    private void putColumnNamesOfEmbeddedType(@Nonnull Class<?> type, @Nullable String tableName, @NonCapturable @Nonnull @NonNullableElements FreezableList<? super SQLQualifiedColumnName> columnNames) throws ConverterNotFoundException, StructureException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnNames(embeddedField.getType(), embeddedField.getName(), tableName, embeddedField.getAnnotations(), columnNames);
        }
    }
    
    private boolean isReferenceAnnotationPresent(@Nullable @NonNullableElements Annotation[] annotations) {
        if (annotations != null) {
            for (@Nonnull Annotation annotation : annotations) {
                if (annotation instanceof References) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private @Nullable <T extends Annotation> T getAnnotation(@Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Class<T> annotationType) {
        for (@Nonnull Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return (T) annotation;
            }
        }
        return null;
    }
    
    @Override
    public void putColumnNames(@Nonnull Class<?> type, @Nullable String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull @NonNullableElements FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        if (isReferenceAnnotationPresent(annotations)) {
            final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
            for (@Nonnull Field embeddedField : fields) {
                final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
                sqlConverter.putColumnNames(embeddedField.getType(), embeddedField.getName(), tableName, embeddedField.getAnnotations(), qualifiedColumnNames);
            }
        } else {
            putColumnNamesOfEmbeddedType(type, tableName, qualifiedColumnNames);
        }
    }
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    private void putColumnDeclarationsOfEmbeddedType(@Nonnull Class<?> type, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull Site site, @Nonnull @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnDeclarations(type, embeddedField.getName(), columnDeclarations, site, annotations);
        }
    }
    
    private void putColumnDeclarationsOfReferencedType(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, ConverterNotFoundException {
        final @Nullable References references = getAnnotation(annotations, References.class);
        final @Nonnull Field referencedField = type.getField(references.columnName());
        
        final @Nonnull SQLConverter<?> referencedFieldConverter = SQL.FORMAT.getConverter(referencedField);
        final @Nullable SQLType sqlType = referencedFieldConverter.getSQLType(referencedField.getType(), referencedField.getAnnotations());
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get(columnName), sqlType, SQLColumnDefinition.of(annotations), SQLColumnConstraint.of(annotations, columnName));
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nullable Site site, @Nonnull @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        Require.that(Convertible.class.isAssignableFrom(type)).orThrow("The field has the type 'Convertible'");
        
        if (isReferenceAnnotationPresent(annotations)) {
            putColumnDeclarationsOfReferencedType(type, columnName, columnDeclarations, annotations);
        } else {
            putColumnDeclarationsOfEmbeddedType(type, columnDeclarations, site, annotations);
        }
    }
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException {
        // TODO: insert values of referenced types into the referenced table
        if (field.isAnnotationPresent(References.class)) {
            for (@Nonnull Field embeddedField : field.getType().getFields()) {
                final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
                final @Nullable Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw StoringException.get(field.getType(), e);
                }
                sqlConverter.insertIntoDependentTable(referencedTable, primaryKeyTableCells, value, embeddedField, site);
            }
        }
    }
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        for (@Nonnull Field embeddedField : field.getType().getFields()) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.createDependentTables(referencedTable, embeddedField, site);
        }
    }
    
    @Override
    public void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        // create tables for all fields in the type
        for (@Nonnull Field embeddedField : field.getType().getFields()) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.createRequiredTables(embeddedField, site);
        }
        if (field.isAnnotationPresent(References.class)) {
            final @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclaration = FreezableArrayList.get();
            for (@Nonnull Field referencedField : field.getType().getFields()) {
                final @Nonnull SQLConverter<?> fieldConverter = SQL.FORMAT.getConverter(referencedField);
                fieldConverter.putColumnDeclarations(referencedField.getType(), referencedField.getName(), columnDeclaration, site, referencedField.getAnnotations());
            }
            final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.with(SQLQualifiedTableName.get(field.getAnnotation(References.class).foreignTable(), site), columnDeclaration);
    
            final @Nonnull String createTableStatementString = createTableStatement.toSQL(SQLDialect.getDialect(), site);
            System.out.println("SQL: " + createTableStatementString);
            Database.getInstance().execute(createTableStatementString);
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
