package net.digitalid.database.conversion.converter.object;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.reflection.ReflectionUtility;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.converter.SQLConverter;
import net.digitalid.database.conversion.exceptions.ConformityViolation;
import net.digitalid.database.conversion.exceptions.NoSQLTypeRepresentationException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.References;
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
 * The purpose of the SQL object converter is to create tables, declare columns, and collect values of a {@link Convertible} object for the conversion of it into the rows of one or more SQL tables.
 */
@Stateless
public class SQLObjectConverter<T extends Convertible> extends SQLConverter<T> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public @Nonnull SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
        throw NoSQLTypeRepresentationException.with(type);
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
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLValues> values) throws FailedValueStoringException, InternalException, StoringException, StructureException, NoSuchFieldException {
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
            converter.collectValues(referencedFieldValue, storableField.getType(), field.getAnnotations(), values);
        }
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    /**
     * Collects the fields of the object and converts them into column names.
     */
    private void putColumnNamesOfEmbeddedType(@Nonnull Class<?> type, @Nullable String tableName, @NonCapturable @Nonnull @NonNullableElements FreezableList<? super SQLQualifiedColumnName> columnNames) throws ConverterNotFoundException, StructureException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnNames(embeddedField.getType(), embeddedField.getName(), tableName, embeddedField.getAnnotations(), columnNames);
        }
    }
    
    /**
     * Returns true if a reference annotation is amoung the array of annotations.
     */
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
    
    /**
     * Returns the annotation specified in the given annotation type or null if the annotation could not be found in the array of annotations.
     */
    @SuppressWarnings("unchecked")
    private @Nullable <A extends Annotation> A getAnnotation(@Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Class<A> annotationType) {
        for (@Nonnull Annotation annotation : annotations) {
            if (annotationType.isInstance(annotation)) {
                return (A) annotation;
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
    
    /**
     * Iterates through the fields of the type and converts each field into a column declaration.
     */
    private void putColumnDeclarationsOfEmbeddedType(@Nonnull Class<?> type, @Nonnull @NonCapturable @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        for (@Nonnull Field embeddedField : fields) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(embeddedField);
            sqlConverter.putColumnDeclarations(type, embeddedField.getName(), annotations, columnDeclarations);
        }
    }
    
    /**
     * Adds the column declaration of a referenced type to the list of column declarations. Throws a StructureException if the referenced column does not exist.
     */
    private void putColumnDeclarationsOfReferencedType(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, ConverterNotFoundException, StructureException {
        final @Nullable References references = getAnnotation(annotations, References.class);
        assert references != null;
        
        final @Nonnull Field referencedField;
        try {
            referencedField = type.getField(references.columnName());
        } catch (NoSuchFieldException e) {
            throw StructureException.get("The column '" + references.columnName() + "' does not exist.");
        }
        final @Nonnull SQLConverter<?> referencedFieldConverter = SQL.FORMAT.getConverter(referencedField);
        final @Nonnull SQLType sqlType = referencedFieldConverter.getSQLType(referencedField.getType(), referencedField.getAnnotations());
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get(columnName), sqlType, SQLColumnDefinition.of(annotations), SQLColumnConstraint.of(annotations, columnName));
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        Require.that(Convertible.class.isAssignableFrom(type)).orThrow("The field has the type 'Convertible'");
        
        if (isReferenceAnnotationPresent(annotations)) {
            putColumnDeclarationsOfReferencedType(type, columnName, columnDeclarations, annotations);
        } else {
            putColumnDeclarationsOfEmbeddedType(type, columnDeclarations, annotations);
        }
    }
    
    /* -------------------------------------------------- Required and Dependent Tables -------------------------------------------------- */
    
    // TODO: How do we detect cycles?
    @Override
    public void createRequiredTables(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        // create tables for all fields in the type
        for (@Nonnull Field field : type.getFields()) {
            if (isReferenceAnnotationPresent(field.getAnnotations())) {
                assert Convertible.class.isInstance(field.getType());
    
                @SuppressWarnings("unchecked")
                Class<? extends Convertible> convertibleType = (Class<? extends Convertible>) field.getType();
                // TODO: Create non-committing?! Creating required tables should be in the same transaction as the original table.
                SQL.create(field.getName(), site, convertibleType);
            }
            
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(field);
            sqlConverter.createRequiredTables(field.getType(), field.getAnnotations(), site);
        }
    }
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        for (@Nonnull Field field : type.getFields()) {
            final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(field);
            sqlConverter.createDependentTables(referencedTable, type, field.getName(), field.getAnnotations());
        }
    }
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws StructureException, StoringException, FailedNonCommittingOperationException {
        for (@Nonnull Field field: type.getFields()) {
            if (!isReferenceAnnotationPresent(annotations)) {
                final @Nonnull SQLConverter<?> sqlConverter = SQL.FORMAT.getConverter(field);
                final @Nullable Object value;
                try {
                    field.setAccessible(true);
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw StoringException.get(field.getType(), e);
                }
                sqlConverter.insertIntoDependentTable(referencedTable, primaryKeyTableCells, value, field.getType(), field.getName(), field.getAnnotations());
            }
        }
    }
    
   
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @Override
    @SuppressWarnings("unchecked")
    public @Nullable T recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        final @Nonnull @NonNullableElements @Frozen ReadOnlyList<Field> fields = ReflectionUtility.getReconstructionFields(type);
        final @Nonnull @NullableElements FreezableArrayList<Object> recoveredValues = FreezableArrayList.get();
        for (@Nonnull Field field : fields) {
            final @Nonnull SQLConverter<?> converter = SQL.FORMAT.getConverter(field.getType());
            final @Nullable Object fieldValue = converter.recoverNullable(field.getType(), result, annotations);
            recoveredValues.add(fieldValue);
        }
        return (T) Converter.recoverNonNullableObject(type, recoveredValues);
    }
    
}
