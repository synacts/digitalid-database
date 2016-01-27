package net.digitalid.database.dialect.table;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

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
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new table with the given name and declaration.
     * 
     * @param tableName the name of the new table.
     */
    protected Table(@Nonnull SQLQualifiedTableName tableName) {
        this.name = tableName;
    }
    
    /**
     * Returns a new table with the given name and declaration.
     * 
     * @param name the name of the new table.
     * 
     * @return a new table with the given name and declaration.
     */
    @Pure
    public static @Nonnull Table get(@Nonnull SQLQualifiedTableName name) {
        return new Table(name);
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
        return site + name.tableName;
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
