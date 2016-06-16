package net.digitalid.database.dialect.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.method.Pure;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLCreateTableStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLPrimaryKeyConstraint;

/**
 * This class models a database table.
 */
@Immutable
public class Table {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Stores the name of this table.
     */
    private final @Nonnull SQLQualifiedTableName name;
    
    /**
     * Returns the name of this table.
     * 
     * @return the name of this table.
     */
    @Pure
    public final @Nonnull SQLQualifiedTableName getName() {
        return name;
    }
    
    /* -------------------------------------------------- Primary Keys -------------------------------------------------- */
    
    private final @Nonnull FreezableArrayList<PrimaryKey> primaryKeys;
    
    public final @Nonnull @Frozen @NonNullableElements ReadOnlyList<PrimaryKey> getPrimaryKeys() {
        return primaryKeys;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private void initializePrimaryKeys(@Nonnull SQLCreateTableStatement createTableStatement) {
        int position = 0;
        for (SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
            final Class<?> type = columnDeclaration.type.getJavaType();
            if (columnDeclaration.columnConstraints != null) {
                for (SQLColumnConstraint columnConstraint : columnDeclaration.columnConstraints) {
                    if (columnConstraint instanceof SQLPrimaryKeyConstraint) {
                        primaryKeys.add(PrimaryKey.with(type, columnDeclaration.qualifiedColumnName.getValue(), position));
                    }
                }
            }
            position++;
        }
        position = 0;
        if (primaryKeys.size() == 0) {
            for (SQLColumnDeclaration columnDeclaration : createTableStatement.columnDeclarations) {
                final Class<?> type = columnDeclaration.type.getJavaType();
                primaryKeys.add(PrimaryKey.with(type, columnDeclaration.qualifiedColumnName.getValue(), position));
                position++;
            }           
        }
    }
    
    /**
     * Creates a new table with the given name and declaration.
     */
    protected Table(@Nonnull SQLCreateTableStatement createTableStatement) {
        this.name = createTableStatement.qualifiedTableName;
        this.primaryKeys = FreezableArrayList.get();
        initializePrimaryKeys(createTableStatement);
    }
    
    /**
     * Returns a new table with the given name and declaration.
     */
    @Pure
    public static @Nonnull Table get(@Nonnull SQLCreateTableStatement createTableStatement) {
        return new Table(createTableStatement);
    }
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of this table with the prefix of the given site.
     * 
     * @param site the site whose prefix is to be used for the returned name.
     * 
     * @return the name of this table with the prefix of the given site.
     */
    @Pure
    public final @Nonnull String getName(@Nonnull Site site) {
        return name.getValue();
    }
    
    public @Nonnull @NonNullableElements FreezableArrayList<SQLValues> filterPrimaryKeyTableCells(@Nonnull @NonNullableElements ReadOnlyList<SQLValues> tableRows) {
        final @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells = FreezableArrayList.get();
        for (@Nonnull SQLValues sqlValues : tableRows) {
            SQLValues values = SQLValues.get();
            for (@Nonnull PrimaryKey primaryKey : primaryKeys) {
                values.addValue(sqlValues.values.get(primaryKey.columnPosition));
            }
            primaryKeyTableCells.add(values);
        }
        return primaryKeyTableCells;
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
