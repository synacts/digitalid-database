package net.digitalid.database.dialect.table;

import java.util.Map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLForeignKeyConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLPrimaryKeyConstraint;
import net.digitalid.database.interfaces.SQLKey;
import net.digitalid.database.interfaces.Site;
import net.digitalid.database.interfaces.Table;

/**
 * This class models a database table.
 */
@Immutable
public class TableImplementation implements Table {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Stores the name of this table.
     */
    private final @Nonnull SQLQualifiedTableName name;
    
    /* -------------------------------------------------- Primary Keys -------------------------------------------------- */
    
    private final @Nonnull ReadOnlyList<@Nonnull SQLKey> primaryKeys;
    
    @Pure
    public final @Nonnull ReadOnlyList<@Nonnull SQLKey> getPrimaryKeys() {
        return primaryKeys;
    }
    
    private final @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull TableImplementation> foreignKeys;
    
    @Pure
    @Override
    public final @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull TableImplementation> getForeignKeys() {
        return foreignKeys;
    }
    
    private final @Nonnull ReadOnlyMap<@Nonnull  String, @Nonnull Integer> typesForColumns;
    
    @Pure
    @Override
    public int getTypeOfColumn(@Nonnull String columnName) {
        return typesForColumns.get(columnName);
    }
    
    private final @Nonnull ReadOnlyMap<@Nonnull  String, @Nonnull Integer> numberOfColumnsForField;
    
    @Pure
    @Override
    public int getNumberOfColumnsForField(@Nonnull String fieldName) {
        return numberOfColumnsForField.get(fieldName);
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new table with the given name and declaration.
     */
    protected TableImplementation(@Nonnull SQLQualifiedTableName name, @Nonnull ReadOnlyList<SQLKey> primaryKeys, @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull TableImplementation> foreignKeys, @Nonnull ReadOnlyMap<@Nonnull  String, @Nonnull Integer> typesForColumns, @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull Integer> numberOfColumnsForField) {
        this.name = name;
        this.primaryKeys = primaryKeys;
        this.foreignKeys = foreignKeys;
        this.typesForColumns = typesForColumns;
        this.numberOfColumnsForField = numberOfColumnsForField;
    }
    
    /* -------------------------------------------------- Primary Keys -------------------------------------------------- */
    
    @Pure
    private static @Nonnull @Frozen ReadOnlyList<@Nonnull SQLKey> initializePrimaryKeys(@Nonnull SQLCreateTableStatement createTableStatement) {
        final @Nonnull FreezableArrayList<@Nonnull SQLKey> primaryKeys = FreezableArrayList.withNoElements();
        int position = 0;
        for (SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
                final CustomType type = columnDeclaration.typeNode.getSQLType().getCustomType();
                if (columnDeclaration.columnConstraints != null) {
                    for (SQLColumnConstraint columnConstraint : columnDeclaration.columnConstraints) {
                        if (columnConstraint instanceof SQLPrimaryKeyConstraint) {
                            primaryKeys.add(SQLKey.with(type, columnDeclaration.columnName.getValue(), position));
                        }
                    }
                }
            position++;
        }
        position = 0;
        if (primaryKeys.size() == 0) {
            for (SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
                final @Nonnull CustomType type = columnDeclaration.typeNode.getSQLType().getCustomType();
                primaryKeys.add(SQLKey.with(type, columnDeclaration.columnName.getValue(), position));
            }
            position++;
        }
        return primaryKeys;
    }
    
    /* -------------------------------------------------- Foreign Keys -------------------------------------------------- */
    
    @Pure
    private static @Nonnull @Frozen ReadOnlyMap<@Nonnull SQLKey, @Nonnull TableImplementation> initializeForeignKeys(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull TableImplementation> preConstructedTables, @Nonnull Site site) {
        final @Nonnull FreezableHashMap<@Nonnull SQLKey, @Nonnull TableImplementation> foreignKeys = FreezableHashMapBuilder.build();
        int position = 0;
        for (SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
            final CustomType type = columnDeclaration.typeNode.getSQLType().getCustomType();
            if (columnDeclaration.columnConstraints != null) {
                for (SQLColumnConstraint columnConstraint : columnDeclaration.columnConstraints) {
                    if (columnConstraint instanceof SQLForeignKeyConstraint) {
                        @Nonnull final CustomAnnotation references = ((SQLForeignKeyConstraint) columnConstraint).references;
                        final @Nonnull String foreignTable = site.getName() + "." + references.get("foreignTable", String.class);
                        for (Map.@Nonnull Entry<@Nonnull String, @Nonnull TableImplementation> preConstructedTableName : preConstructedTables.entrySet()) {
                            if (preConstructedTableName.getKey().equals(foreignTable)) {
                                foreignKeys.put(SQLKey.with(type, columnDeclaration.columnName.getValue(), position), preConstructedTableName.getValue());
                            }
                        }
                    }
                }
            }
            position++;
        }
        return foreignKeys;
    }
    
    /* -------------------------------------------------- Get Table -------------------------------------------------- */
    
    /**
     * Returns a new table with the given name and declaration.
     */
    @Pure
    public static @Nonnull TableImplementation get(@Nonnull SQLCreateTableStatement createTableStatement, @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull TableImplementation> preConstructedTables, @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull Integer> numberOfColumnsForField, @Nonnull Site site) {
        final @Nonnull SQLQualifiedTableName name = createTableStatement.qualifiedTableName;
        final @Nonnull ReadOnlyList<@Nonnull SQLKey> primaryKeys = initializePrimaryKeys(createTableStatement);
        final @Nonnull ReadOnlyMap<@Nonnull SQLKey, @Nonnull TableImplementation> foreignKeys = initializeForeignKeys(createTableStatement, preConstructedTables, site);
        @Nonnull ReadOnlyMap<@Nonnull  String, @Nonnull Integer> typesForColumns = initializeTypesForColumns(createTableStatement);
        return new TableImplementation(name, primaryKeys, foreignKeys, typesForColumns, numberOfColumnsForField);
    }
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this table with the prefix of the given site.
     */
    @Pure
    @Override
    public final @Nonnull String getName() {
        return name.tableName;
    }
    
    @Pure
    public @Nonnull @NonNullableElements FreezableArrayList<SQLValues> filterPrimaryKeyTableCells(@Nonnull @NonNullableElements ReadOnlyList<SQLValues> tableRows) {
        final @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells = FreezableArrayList.withNoElements();
        for (@Nonnull SQLValues sqlValues : tableRows) {
            SQLValues values = SQLValues.get();
            for (@Nonnull SQLKey primaryKey : primaryKeys) {
                values.addValue(sqlValues.values.get(primaryKey.columnPosition));
            }
            primaryKeyTableCells.add(values);
        }
        return primaryKeyTableCells;
    }
    
    @Pure
    private static @Nonnull ReadOnlyMap<@Nonnull String, @Nonnull Integer> initializeTypesForColumns(@Nonnull SQLCreateTableStatement createTableStatement) {
        @Nonnull FreezableHashMap<@Nonnull String, @Nonnull Integer> typesForColumns = FreezableHashMapBuilder.buildWithInitialCapacity(createTableStatement.columnDeclarations.size());
        for (@Nonnull SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
            typesForColumns.put(columnDeclaration.columnName.getValue(), columnDeclaration.typeNode.getSQLType().getCode());
        }
        return typesForColumns;
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    // TODO: remove after creation and deletion of tables is handled by converter.
//    /**
//     * Creates this table on the given site.
//     * 
//     * @param site the site on which to create this table.
//     * 
//     * @require !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
//     */
//    @Locked
//    @NonCommitting
//    public final void create(@Nonnull Site site) throws FailedOperationException {
//        try (@Nonnull Statement statement = Database.createStatement()) {
//            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + getName(site) + " (" + declaration.toString() + ", PRIMARY KEY (" + declaration.getPrimaryKeySelection() + ")" + declaration.getForeignKeys(site) + ")");
//            if (isSiteSpecific()) { Database.onInsertIgnore(statement, getName(site), declaration.getPrimaryKeyColumnNames().toArray()); }
//            declaration.executeAfterCreation(statement, this, site, true, name);
//        } catch (@Nonnull SQLException exception) {
//            throw FailedUpdateExecutionException.get(exception);
//        }
//    }
//    
//    /**
//     * Deletes this table on the given site.
//     * 
//     * @param site the site on which to delete this table.
//     * 
//     * @require !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
//     */
//    @Locked
//    @NonCommitting
//    public final void delete(@Nonnull Site site) throws FailedOperationException {
//        try (@Nonnull Statement statement = Database.createStatement()) {
//            declaration.executeBeforeDeletion(statement, this, site, true, name);
//            if (isSiteSpecific()) { Database.onInsertNotIgnore(statement, getName(site)); }
//            statement.executeUpdate("DROP TABLE IF EXISTS " + getName(site));
//        } catch (@Nonnull SQLException exception) {
//            throw FailedUpdateExecutionException.get(exception);
//        }
//    }
    
}
