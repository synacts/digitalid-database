package net.digitalid.database.conversion.value.iterable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
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
public class SQLCollectionsConverter extends SQLConverter<Collection<?>> {
    
    @Override public SQLType getSQLType(Field field) {
        return null;
    }
    
    private List<FreezableArrayList<SQLValues>> deepCopy(int n, FreezableArrayList<SQLValues> list) {
        List<FreezableArrayList<SQLValues>> copies = FreezableArrayList.getWithCapacity(n);
        for (int i = 0; i < n; i++) {
            copies.add(FreezableArrayList.<SQLValues>getWithCapacity(list.size()));
        }
        for (SQLValues c : list) {
            for (FreezableArrayList<SQLValues> copy : copies) {
                copy.add(c.copy());
            }
        }
        return copies;
    }
    
    /**
     * Returns the type declared in the @GenericTypes annotation.
     */
    private Class<?> getGenericType(@Nonnull @NonNullableElements Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof GenericTypes) {
                return ((GenericTypes) annotation).value()[0];
            }
        }
        // TODO: improve exception
        throw new RuntimeException("Expected a @GenericType annotation for the collection.");
    }
    
    @Override 
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nullable Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        // TODO: assert that object is of type collection
        final @Nullable Collection<?> collection = (Collection<?>) object;
        
        final @Nonnull Class<?> genericType = getGenericType(annotations);
        List<FreezableArrayList<SQLValues>> copiesOfValuesList = deepCopy(collection.size(), valuesList);
        if (collection != null) {
            FreezableArrayList<SQLValues> newList = FreezableArrayList.get();
            int i = 0;
            for (@Nullable Object element : collection) {
                final @Nonnull FreezableArrayList<SQLValues> copyOfValuesList = copiesOfValuesList.get(i);
                if (Collection.class.isAssignableFrom(genericType)) {
                    SQL.FORMAT.getConverter(Integer.class).collectValues(i, Integer.class, copyOfValuesList, null);
                }
                SQL.FORMAT.getConverter(genericType).collectValues(element, genericType, copyOfValuesList, genericType.getAnnotations());
                newList.addAll(copyOfValuesList);
                i++;
            }
            valuesList.clear();
            valuesList.addAll(newList);
        } else {
            SQL.FORMAT.getConverter(genericType).collectValues(null, genericType, valuesList, null);
        }
    }
    
    @Override 
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get("_" + field.getName() + "_index", tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
        final @Nonnull Class<?> genericType = getGenericType(field.getAnnotations());
        SQL.FORMAT.getConverter(genericType).putColumnNames(field, qualifiedColumnNames);
    }
    
    @Override 
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: assert that field is of type collections.
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get(field.getName()), SQLType.INTEGER32, FreezableArrayList.<SQLColumnDefinition>get(), FreezableArrayList.<SQLColumnConstraint>get());
        columnDeclarations.add(columnDeclaration);
        final @Nonnull Class<?> genericType = getGenericType(field.getAnnotations());
        SQL.FORMAT.getConverter(genericType).putColumnDeclarations(field, columnDeclarations);
    }
    
    @Override 
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
    
}
