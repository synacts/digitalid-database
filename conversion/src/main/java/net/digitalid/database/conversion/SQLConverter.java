package net.digitalid.database.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableHashSet;
import net.digitalid.utility.collections.freezable.FreezableSet;
import net.digitalid.utility.collections.readonly.ReadOnlySet;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Stateless;

import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    protected abstract void collectNonNullValues(@Nonnull Object object, @NonCapturable @Nonnull SQLValues values) throws FailedValueStoringException, ConverterNotFoundException, StoringException;
    
    protected void collectValues(@Nullable Object object, @NonCapturable @Nonnull SQLValues values) throws StoringException, ConverterNotFoundException, FailedValueStoringException {
        if (object == null) {
            // TODO: is it ok to use boolean here?
            values.addValue(SQLBooleanLiteral.get(null));
        } else {
            collectNonNullValues(object, values);
        }
    }
    
    protected abstract void putColumnNames(@Nonnull Field field, @Nullable SQLQualifiedTableName tableName, @NonCapturable @Nonnull FreezableArrayList<SQLQualifiedColumnName> qualifiedColumnNames);
    
    // TODO: Cache column declarations in caller.
    protected abstract void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations);
    
    /*
     * TODO: move to SQL
     */
    private static class Cache {
        private static final @Nonnull @NonNullableElements Map<Class<?>,ReadOnlyList<SQLQualifiedColumnName>> qualifiedColumnNamesCache = new HashMap<>();
    
        public static boolean containsQualifiedColumnNames(@Nonnull Class<?> type) {
            return qualifiedColumnNamesCache.containsKey(type);
        }
    
        public static @Nonnull @NonNullableElements ReadOnlyList<SQLQualifiedColumnName> getQualifiedColumnNames(@Nonnull Class<?> type) {
            return qualifiedColumnNamesCache.get(type);
        }
        
        public static void setQualifiedColumnNames(@Nonnull Class<?> type, @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames) {
            qualifiedColumnNamesCache.put(type, qualifiedColumnNames);
        }
    }
    
    /*
     * TODO: move to SQL
     */
    protected SQLInsertStatement insert(@Nullable T object, @Nonnull Class<T> type, @Nonnull SQLQualifiedTableName tableName) throws FailedValueStoringException, ConverterNotFoundException, StoringException {
        // TODO: what about prefixes?!
        
        final @Nonnull @NonNullableElements Field[] fields = type.getFields();
        for (@Nonnull Field field : fields) {
            final Class<?> fieldType = field.getType();
            final @Nullable @NonNullableElements @Frozen ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames;
            if (Cache.containsQualifiedColumnNames(fieldType)) {
                qualifiedColumnNames = Cache.getQualifiedColumnNames(fieldType);
            } else {
                @Nonnull @NonNullableElements @NonFrozen FreezableArrayList<SQLQualifiedColumnName> initialQualifiedColumnNamesList = FreezableArrayList.get();
                putColumnNames(field, tableName, initialQualifiedColumnNamesList);
                qualifiedColumnNames = initialQualifiedColumnNamesList.freeze();
                Cache.setQualifiedColumnNames(fieldType, qualifiedColumnNames);
            }
    
            final @Nullable Object value;
            if (object == null) {
                value = null;
            } else {
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw StoringException.get(type, e);
                }
            }
            final @Nonnull @NullableElements SQLValues values = SQLValues.get();
            collectValues(value, values);
        }
        return SQLInsertStatement.get(tableName, qualifiedColumnNames, values);
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    protected @Nonnull Object recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        @Nullable Object object = recoverNullable(type, result);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    protected abstract @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException;
    
    private @Nonnull @Frozen ReadOnlySet<SQLColumnConstraint> getConstraintsFromAnnotations(@Nonnull Field field) {
        final @Nonnull @NonNullableElements Annotation[] annotations = field.getAnnotations();
        final @Nonnull @NonNullableElements FreezableSet<SQLColumnConstraint> columnConstraints = FreezableHashSet.get();
        for (@Nonnull Annotation annotation : annotations) {
            // TODO: discuss if it is ok to process the annotation in the AST. Alternatively, the constraints must be moved out of the dialect and into
            // the converter package. The columnConstraint should then only accept the SQL expression, if needed.
            columnConstraints.add(SQLColumnConstraint.of(annotation, field));
        }
        return columnConstraints.freeze();
    }
    
    public @Nonnull SQLColumnDeclaration createColumnDeclaration(@Nonnull String tableName, @Nonnull Field field) throws InternalException {
        @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), tableName);
        // TODO: move SQLType.of(...) to converter pkg
        @Nonnull SQLType sqlType = SQLType.of(field.getType());
        @Nonnull @Frozen ReadOnlySet<SQLColumnConstraint> columnConstraints = getConstraintsFromAnnotations(field);
        return SQLColumnDeclaration.with(qualifiedColumnName, sqlType, columnConstraints);
    }
    
}
