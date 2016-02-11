package net.digitalid.database.conversion.value.iterable;

import net.digitalid.net.root.Copyable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.conversion.annotations.GenericTypes;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQL;
import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.Embedd;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 * This abstract class implements methods that are used to convert iterable data structures into SQL.
 */
public class SQLIterableConverter<T> extends SQLConverter<T> {
    
    /**
     * Returns true if the @Embedd annotation was found, false otherwise.
     */
    protected boolean isAnnotatedWithEmbedd(@Nonnull @NonNullableElements Annotation[] annotations) {
        for (@Nonnull Annotation annotation : annotations) {
            if (annotation instanceof Embedd) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the type declared in the @GenericTypes annotation.
     */
    protected Class<?> getGenericType(@Nullable @NonNullableElements Annotation[] annotations) {
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation instanceof GenericTypes) {
                    return ((GenericTypes) annotation).value()[0];
                }
            }
        }
        // TODO: improve exception
        throw new RuntimeException("Expected a @GenericType annotation for the collection.");
    }
    
    /**
     * Recursively walks through GenericType annotations until it finds a type that is not annotated with @GenericType.
     * Then returns the type.
     */
    protected Class<?> getColumnType(@Nullable @NonNullableElements Annotation[] annotations) {
        @Nonnull Class<?> genericType = getGenericType(annotations);
        while (genericType.isAnnotationPresent(GenericTypes.class)) {
            genericType = getColumnType(genericType.getAnnotations());
        }
        return genericType;
    }
    
    /**
     * Returns n copies of the given list.
     */
    protected <T extends Copyable<T>> List<FreezableArrayList<T>> deepCopy(int n, ReadOnlyList<T> list) {
        List<FreezableArrayList<T>> copies = FreezableArrayList.getWithCapacity(n);
        for (int i = 0; i < n; i++) {
            copies.add(FreezableArrayList.<T>getWithCapacity(list.size()));
        }
        for (T c : list) {
            for (FreezableArrayList<T> copy : copies) {
                copy.add(c.copy());
            }
        }
        return copies;
    }
    
    @Override 
    public @Nullable SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) {
        if (isAnnotatedWithEmbedd(annotations)) {
            return SQL.FORMAT.getSQLConverter(getGenericType(annotations)).getSQLType(type, annotations);
        }
        return null;
    }
 
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nonnull @NonNullableElements Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        
    }
    
    @Override
    public void putColumnNames(@Nonnull Class<?> type, @Nullable String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nullable String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, Site site, @Nullable @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        
    }
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException {
        
    }
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        
    }
    
    @Override
    public void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        
    }
    
    @Nullable
    @Override
    public Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
}
