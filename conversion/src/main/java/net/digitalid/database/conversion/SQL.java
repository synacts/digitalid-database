package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.elements.NullableElements;
import net.digitalid.utility.conversion.Convertible;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.state.Stateless;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.statement.select.SQLWhereClause;
import net.digitalid.database.dialect.ast.statement.table.create.SQLReference;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
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
    
    public static Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull @NonNullableElements Class<? extends Convertible>... columnGroups) throws InternalException {
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
    public static Table create(@Nonnull String tableName, @Nonnull Site site, @Nonnull @NonNullableElements Class<? extends Convertible>[] columnGroups, @Nonnull SQLReference[] references) throws InternalException {
/*        final @Nonnull SQLQualifiedTableName qualifiedTableName = SQLQualifiedTableName.get(tableName, site);
        final @Nonnull SQLCreateTableStatement createTableStatement = SQLCreateTableStatement.get(qualifiedTableName);
        for (@Nonnull Class<? extends Convertible> columnGroup : columnGroups) {
            for (@Nonnull Field field : columnGroup.getFields()) {
                @Nonnull SQLConverter<?> sqlConverter = FORMAT.getConverter(field);
                @Nonnull SQLColumnDeclaration columnDeclaration = sqlConverter.createColumnDeclaration(field);
                createTableStatement.addColumnDeclaration(columnDeclaration);
            }
        }
        final @Nonnull String createTableStatementString = createTableStatement.toSQL(SQLDialect.getDialect(), site);
        Database.getInstance().execute(createTableStatementString);
        return Table.get(qualifiedTableName);*/
        return null;
    }
    
    /**
     * Stores a nullable object into a SQL table.
     */
    // TODO: do we even care about nullable / non-nullable??
    public static void insert(@Nonnull Table table, @Nullable @NullableElements Convertible[] convertibles, @Nonnull Site site) throws StoringException, InternalException {
/*        // TODO: cache AST for convertible
        // TODO: if cached AST is retrieved, the value in site must be changed so that we can update the schema without re-generating the AST.
        final @Nonnull SQLInsertStatement insertStatement = SQLInsertStatement.get(table.getName());
        
        Class<? extends Convertible>[] columnGroups = table.getColumnGroups();
        
        for (@Nonnull Class<? extends Convertible> columnGroup : columnGroups) {
            for (@Nonnull Field field : columnGroup.getFields()) {
                final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), table.getName().tableName);
                FORMAT.getConverter(field);
            }
        }*/
    }
    
}
