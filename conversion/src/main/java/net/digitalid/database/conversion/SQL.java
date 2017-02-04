package net.digitalid.database.conversion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.Shared;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.functional.iterables.InfiniteIterable;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.conversion.utility.SQLConversionUtility;
import net.digitalid.database.dialect.expression.SQLParameter;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.insert.SQLExpressionsBuilder;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.statement.insert.SQLInsertStatementBuilder;
import net.digitalid.database.dialect.statement.insert.SQLRows;
import net.digitalid.database.dialect.statement.insert.SQLRowsBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclarationBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.statement.table.create.SQLCreateTableStatementBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLTypeBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.Database;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;
import net.digitalid.database.unit.Unit;

/**
 * This class simplifies the execution of SQL statements.
 */
@Utility
public abstract class SQL {
    
    /* -------------------------------------------------- Create Table -------------------------------------------------- */
    
    // TODO: move to a column declaration utility class
    @Pure
    private static <@Unspecifiable TYPE> void fillColumnDeclarations(@Nonnull Converter<TYPE, ?> converter, @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations, boolean mustBeNullable) {
        final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
        for (@Nonnull CustomField field : fields) {
            @Nonnull CustomType customType = field.getCustomType();
            if (!customType.isCompositeType()) {
                boolean primitive = true;
                if (customType.isObjectType()) {
                    final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) customType;
                    if (!customConverterType.getConverter().isPrimitiveConverter()) {
                        fillColumnDeclarations(customConverterType.getConverter(), columnDeclarations, mustBeNullable || !SQLConversionUtility.isNotNull(field));
                        return;
                    } else { // otherwise we have a boxed primitive type
                        @Nullable CustomType primitiveType = SQLConversionUtility.getEmbeddedPrimitiveType(customConverterType);
                        Require.that(primitiveType != null).orThrow("The custom converter type $ is expected to embed a primitive type", customConverterType);
                        assert primitiveType != null; // suppress compiler warning
                        customType = primitiveType;
                        primitive = false;
                    }
                }
                final @Nonnull SQLColumnDeclaration sqlColumnDeclaration = SQLColumnDeclarationBuilder
                        .withName(SQLColumnNameBuilder.withString(field.getName()).build())
                        .withType(SQLTypeBuilder.withType(customType).build())
                        .withNotNull(!mustBeNullable && (primitive || SQLConversionUtility.isNotNull(field)))
                        .withDefaultValue(SQLConversionUtility.getDefaultValue(field))
                        .withPrimaryKey(SQLConversionUtility.isPrimaryKey(field))
                        .withReference(SQLConversionUtility.isForeignKey(field))
                        .withUnique(SQLConversionUtility.isUnique(field))
                        .withCheck(SQLConversionUtility.getCheck(field))
                        .build();
                columnDeclarations.add(sqlColumnDeclaration);
            } else {
                throw new UnsupportedOperationException("Composite types such as iterables or maps are currently not supported by the SQL encoders");
            }
        }
    }
    
    /**
     * Creates a table for the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void createTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withNoElements();
        fillColumnDeclarations(converter, columnDeclarations, false);
        final @Nonnull SQLQualifiedTable tableName = SQLExplicitlyQualifiedTableBuilder.withTable(SQLTableNameBuilder.withString(converter.getTypeName()).build()).withSchema(SQLSchemaNameBuilder.withString(unit.getName()).build()).build();
        SQLCreateTableStatementBuilder.@Nonnull InnerSQLCreateTableStatementBuilder sqlCreateTableStatementBuilder = SQLCreateTableStatementBuilder.withTable(tableName).withColumnDeclarations(ImmutableList.withElementsOf(columnDeclarations));
        // TODO: what if the referenced table is in another unit?
        final @Nonnull ImmutableList<SQLTableConstraint> tableConstraints = SQLConversionUtility.getTableConstraints(converter, unit);
        if (!tableConstraints.isEmpty()) {
            sqlCreateTableStatementBuilder = sqlCreateTableStatementBuilder.withTableConstraints(tableConstraints);
        }
        final @Nonnull SQLCreateTableStatement createTableStatement = sqlCreateTableStatementBuilder.build();
        Database.instance.get().execute(createTableStatement, unit);
        Database.instance.get().commit();
        // TODO
//        final @Nonnull String tableName = converter.getTypeName();
//        final @Nonnull SQLCreateTableColumnDeclarations columnDeclarations = SQLCreateTableColumnDeclarations.get(tableName);
//        for (@Nonnull CustomField field : converter.getFields(Representation.INTERNAL)) {
//            columnDeclarations.setField(field);
//        }
//        
//        final @Nonnull SQLOrderedStatements<SQLCreateTableStatement, ? extends SQLCreateTableColumnDeclarations> orderedCreateStatements = columnDeclarations.getOrderedStatements();
//    
//        @Nullable TableImplementation mainTable = null;
//        final @Nonnull FreezableHashMap<@Nonnull String, @Nonnull TableImplementation> constructedTables = FreezableHashMapBuilder.build();
//        for (@Nonnull SQLCreateTableStatement sqlCreateTableStatement : orderedCreateStatements.getStatementsOrderedByExecution()) {
//            final @Nonnull String createTableStatementString = SQLDialect.getDialect().transcribe(site, sqlCreateTableStatement);
//            sqlCreateTableStatement.columnDeclarations.size();
//            
//            final @Nonnull TableImplementation table = TableImplementation.get(sqlCreateTableStatement, constructedTables, columnDeclarations.getNumberOfColumnsForField(), site);
//            constructedTables.put(table.getName(), table);
//            if (table.getName().equals(tableName)) {
//                mainTable = table;
//            }
//            Tables.add(table);
//            Log.debugging(createTableStatementString);
//            DatabaseUtility.getInstance().execute(createTableStatementString);
//        }
//        if (mainTable == null) {
//            DatabaseUtility.rollback();
//            throw UncheckedException.with("The conversion failed due to a failure in the ordering of tables.");
//        }
//        DatabaseUtility.commit();
//        return mainTable;
    }
    
    /* -------------------------------------------------- Drop Table -------------------------------------------------- */
    
    /**
     * Drops the table of the given converter in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static void dropTable(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Insert -------------------------------------------------- */
    
    /**
     * Inserts the given object with the given converter into its table in the given unit.
     */
    @NonCommitting
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insert(@Nonnull TYPE object, @Nonnull Converter<TYPE, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columns = FreezableArrayList.withNoElements();
        SQLConversionUtility.fillColumnNames(converter, columns);
        final @Nonnull ImmutableList<@Nonnull SQLParameter> row = ImmutableList.withElementsOf(InfiniteIterable.repeat(SQLParameter.INSTANCE).limit(columns.size()));
        final @Nonnull SQLRows rows = SQLRowsBuilder.withRows(ImmutableList.withElements(SQLExpressionsBuilder.withExpressions(row).build())).build();
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(converter.getTypeName()).build();
        final @Nonnull SQLSchemaName schema = SQLSchemaNameBuilder.withString(unit.getName()).build();
        final @Nonnull SQLQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
        final SQLInsertStatement sqlInsertStatement = SQLInsertStatementBuilder.withTable(qualifiedTable).withColumns(ImmutableList.withElementsOf(columns)).withValues(rows).build();
    
        final @Nonnull SQLActionEncoder actionEncoder = Database.instance.get().getEncoder(sqlInsertStatement, unit);
        converter.convert(object, actionEncoder);
        actionEncoder.execute();
    }
    
    /**
     * Inserts or updates the given object with the given converter into its table in the given unit.
     */
    @Committing
    @PureWithSideEffects
    public static <@Unspecifiable TYPE> void insertOrUpdate(@Nonnull TYPE object, @Nonnull Converter<TYPE, ?> converter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Update -------------------------------------------------- */
    
    /**
     * Updates the columns of the given converter to the values of the given object with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the update and the where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable UPDATE_TYPE, @Unspecifiable WHERE_TYPE> void update(@Nullable UPDATE_TYPE updateObject, @Nonnull Converter<UPDATE_TYPE, ?> updateConverter, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Delete -------------------------------------------------- */
    
    /**
     * Deletes the entries of the given converter's table with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for the where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable WHERE_TYPE> void delete(@Nonnull Converter<?, ?> deleteConverter, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException {
        // TODO
    }
    
    /* -------------------------------------------------- Select -------------------------------------------------- */
    
    /**
     * Returns the entries of the given converter's table with the given where condition in the given unit.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the select and where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static @Capturable <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED, @Unspecifiable WHERE_TYPE> @Nonnull @NonNullableElements @NonFrozen FreezableList<SELECT_TYPE> selectAll(@Nonnull Converter<SELECT_TYPE, PROVIDED> selectConverter, @Shared PROVIDED provided, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException, RecoveryException {
        // TODO
        return FreezableArrayList.withNoElements();
    }
    
    /**
     * Returns the first entry of the given converter's table with the given where condition in the given unit or null if there is no such entry.
     */
    @Committing
    @PureWithSideEffects
    @TODO(task = "Probably add a prefix parameter for both the select and where converter.", date = "2017-01-22", author = Author.KASPAR_ETTER)
    public static <@Unspecifiable SELECT_TYPE, @Specifiable PROVIDED, @Unspecifiable WHERE_TYPE> @Nullable SELECT_TYPE selectFirst(@Nonnull Converter<SELECT_TYPE, PROVIDED> selectConverter, @Shared PROVIDED provided, @Nullable WHERE_TYPE whereObject, @Nullable Converter<WHERE_TYPE, ?> whereConverter, @Nonnull Unit unit) throws DatabaseException, RecoveryException {
        final @Nonnull FreezableList<SELECT_TYPE> results = selectAll(selectConverter, provided, whereObject, whereConverter, unit);
        if (results.isEmpty()) { return null; } else { return results.getFirst(); }
    }
    
//    @Pure
//    @NonCommitting
//    private static <T> @Nonnull SQLDecoder getSelectionResult(@Nonnull Converter<T, ?> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site) throws DatabaseException {
//        final @Nonnull SQLOrderedStatements<@Nonnull SQLSelectStatement, @Nonnull SQLSelectFromTableColumnDeclarations> orderedSelectStatements = SQLOrderedStatementCache.INSTANCE.getOrderedSelectStatements(converter);
//        final @Nonnull @NonEmpty ReadOnlyList<@Nonnull SQLSelectStatement> statementsOrderedByExecution = orderedSelectStatements.getStatementsOrderedByExecution();
//        final @Nonnull SQLSelectStatement selectStatement = statementsOrderedByExecution.getFirst();
//        
//        if (statementsOrderedByExecution.size() > 1) {
//            throw new UnsupportedOperationException("Querying referenced tables is not yet supported.");
//        }
//        
//        if (whereClauseExpression != null) {
//            final @Nonnull SQLWhereClause whereClause = SQLWhereClause.get(whereClauseExpression);
//            selectStatement.setWhereClause(whereClause);
//        }
//    
//        final @Nonnull String selectIntoTableStatementString = selectStatement.toPreparedStatement(SQLDialect.getDialect(), site);
//        Log.debugging(selectIntoTableStatementString);
//        final @Nonnull SQLDecoder decoder = DatabaseUtility.getInstance().executeSelect(selectIntoTableStatementString);
//        return decoder;
//    }
//    
//    /**
//     * Builds and executes an SQL select statement based on the given converter, site and where clause expression. Returns exactly one recovered object.
//     */
//    @Pure
//    @NonCommitting
//    public static <T, E> @Nullable T select(@Nonnull Converter<T, E> converter, @Nullable SQLBooleanExpression whereClauseExpression, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
//        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, whereClauseExpression, site);
//        
//        if (!decoder.moveToNextRow()) {
//            return null;
//        }
//        final @Nullable T recoveredObject = converter.recover(decoder, externallyProvided);
//        Require.that(!decoder.moveToNextRow()).orThrow("Not all of the rows have been processed.");
//        
//        return recoveredObject;
//    }
//    
//    /**
//     * Builds and executes an SQL select statement based on the given converter and site. Returns a list of recovered objects.
//     */
//    @Pure
//    @NonCommitting
//    public static <T, E> Set<T> export(@Nonnull Converter<T, E> converter, @Nonnull Site<?> site, E externallyProvided) throws DatabaseException {
//        final @Nonnull SQLDecoder decoder = getSelectionResult(converter, null, site);
//        
//        Set<T> recoveredObjects = FreezableLinkedHashSetBuilder.build();
//        while (decoder.moveToNextRow()) {
//            decoder.moveToFirstColumn();
//            final @Nonnull T recoveredObject = converter.recover(decoder, externallyProvided);
//            recoveredObjects.add(recoveredObject);
//        }
//        
//        return recoveredObjects;
//    }
    
}
