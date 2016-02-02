package net.digitalid.database.conversion;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.conversion.exceptions.ConformityViolation;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLReference;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 */
@Stateless
public final class SQL {
    
     /* -------------------------------------------------- Format -------------------------------------------------- */
    
    /**
     * The format object of SQL, which contains the SQL converters.
     */
    public static final @Nonnull SQLFormat FORMAT = new SQLFormat();
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */

    /**
     * Recovers a nullable, convertible object from a SQL table.
     */
    public static @Nullable Convertible recoverNullable(@Nonnull SQLWhereClause whereClause, @Nonnull Class<? extends Convertible> type, @Nonnull Site schema) throws InternalException, RecoveryException, CorruptNullValueException, FailedValueRestoringException {
/*        ConverterAnnotations converterAnnotations = Converter.getAnnotations(type);

        Table table = Table.get(SQLName.get(type.getSimpleName()));
        SelectionResult result = table.select(schema).where(whereClause);
        Object object = SQLFormat.CONVERTIBLE_CONVERTER.recoverNullable(type, result);
        if (!type.isInstance(object)) {
            throw RecoveryException.get(type, "The converter failed to recover the object of type '" + type + "' from the SQL table");
        }
        return type.cast(object);*/
        return null;
    }

    /**
     * Recovers a non-nullable, convertible object from a SQL table.
     */
    public static @Nonnull Convertible recoverNonNullable(@Nonnull SQLWhereClause whereClause, @Nonnull Class<? extends Convertible> type, @Nonnull Site schema) throws InternalException, RecoveryException, CorruptNullValueException, FailedValueRestoringException {
/*        final @Nullable Convertible convertible = recoverNullable(whereClause, type, schema);
        if (convertible == null) {
            throw CorruptNullValueException.get();
        }
        return convertible;*/
        return null;
    }
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    public static @Nonnull Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull @NonNullableElements Class<? extends Convertible>... columnGroups) throws InternalException, FailedNonCommittingOperationException {
        return create(tableName, site, columnGroups, new SQLReference[0]);
    }
    
    /**
     * Creates a table with the given table name at the given site which is capable of storing a
     * set of convertible object specified through the columnGroup parameter and references other tables
     * through the given references parameter.
     * An SQL statement is first constructed into an SQL abstract syntax tree. Afterwards, the AST is
     * transcribed into an SQL string using the loaded dialect. Finally, the SQL string is forwarded to
     * the database instance, which executes the statement. Upon successful execution, a table object is 
     * returned. It may be used for other calls to the SQL class as a reference.
     */
    public static @Nonnull Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull @NonNullableElements Class<? extends Convertible>[] columnGroups, @Nonnull SQLReference[] references) throws InternalException, FailedNonCommittingOperationException {
        final @Nonnull SQLQualifiedTableName qualifiedTableName = SQLQualifiedTableName.get(tableName, site);
        @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDeclaration> columnDeclarations = FreezableArrayList.get();
        for (@Nonnull Class<? extends Convertible> columnGroup : columnGroups) {
            for (@Nonnull Field field : columnGroup.getFields()) {
                @Nonnull SQLConverter<?> sqlConverter = FORMAT.getConverter(field);
                try {
                    sqlConverter.putColumnDeclarations(field, columnDeclarations);
                } catch (StructureException | NoSuchFieldException e) {
                    throw ConformityViolation.with("Failed to convert the field '" + field.getName() + "' due to conformity problems.", e);
                }
            }
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.with(qualifiedTableName, columnDeclarations);
        final @Nonnull String createTableStatementString = createTableStatement.toSQL(SQLDialect.getDialect(), site);
        Database.getInstance().execute(createTableStatementString);
        return Table.get(qualifiedTableName);
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    private static class Cache {
        private static final @Nonnull @NonNullableElements Map<Class<?>, FreezableList<SQLQualifiedColumnName>> qualifiedColumnNamesCache = new HashMap<>();
    
        public static boolean containsQualifiedColumnNames(@Nonnull Class<?> type) {
            return qualifiedColumnNamesCache.containsKey(type);
        }
    
        public static @Nonnull @NonNullableElements FreezableList<SQLQualifiedColumnName> getQualifiedColumnNames(@Nonnull Class<?> type) {
            return qualifiedColumnNamesCache.get(type);
        }
        
        public static void setQualifiedColumnNames(@Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableList<SQLQualifiedColumnName> qualifiedColumnNames) {
            qualifiedColumnNamesCache.put(type, qualifiedColumnNames);
        }
    }
    
    private static @Nonnull @NullableElements SQLValues getSQLValues(@Nullable Convertible object, @Nonnull @NonNullableElements Field[] fields) throws InternalException, FailedValueStoringException, StoringException {
        final @Nonnull @NullableElements SQLValues values = SQLValues.get();
        for (@Nonnull Field field : fields) {
            final @Nonnull SQLConverter<?> sqlFieldConverter = FORMAT.getConverter(field);
            final @Nullable Object value;
            if (object == null) {
                value = null;
            } else {
                try {
                    value = field.get(object);
                } catch (IllegalAccessException e) {
                    throw StoringException.get(field.getType(), e);
                }
            }
            try {
                sqlFieldConverter.collectValues(value, field.getType(), values);
            } catch (StructureException | NoSuchFieldException e) {
                throw ConformityViolation.with("Failed to convert the field '" + field.getName() + "' due to conformity problems.", e);
            }
        }
        return values;
    }
    
    public static void insert(@Nullable Convertible object, @Nonnull Class<? extends Convertible> type, @Nonnull String tableName, @Nonnull Site site) throws FailedNonCommittingOperationException, InternalException, StoringException {
        // TODO: what about prefixes?!
        
        final @Nonnull @NonNullableElements Field[] fields = type.getFields();
        final @Nullable @NonNullableElements @NonFrozen FreezableList<SQLQualifiedColumnName> qualifiedColumnNames;
        
        if (Cache.containsQualifiedColumnNames(type)) {
            qualifiedColumnNames = Cache.getQualifiedColumnNames(type);
        } else {
            qualifiedColumnNames = FreezableArrayList.get();
            for (@Nonnull Field field : fields) {
                final @Nonnull SQLConverter<?> sqlFieldConverter = FORMAT.getConverter(field);
                try {
                    sqlFieldConverter.putColumnNames(field, tableName, qualifiedColumnNames);
                } catch (StructureException e) {
                    throw ConformityViolation.with("Failed to convert the field '" + field.getName() + "' due to conformity problems.", e);
                }
                final Class<?> fieldType = field.getType();
                if (Cache.containsQualifiedColumnNames(fieldType)) {
                    qualifiedColumnNames.addAll(Cache.getQualifiedColumnNames(fieldType));
                } else {
                    Cache.setQualifiedColumnNames(fieldType, qualifiedColumnNames);
                }
            }
        }
        final @Nonnull @NullableElements SQLValues values = getSQLValues(object, fields);
        SQLInsertStatement sqlInsertStatement = SQLInsertStatement.get(SQLQualifiedTableName.get(tableName, site), qualifiedColumnNames, values);
        
        final @Nonnull String createTableStatementString = sqlInsertStatement.toSQL(SQLDialect.getDialect(), site);
        Database.getInstance().execute(createTableStatementString);
    }
    
}
