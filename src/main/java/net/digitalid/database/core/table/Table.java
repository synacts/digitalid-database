package net.digitalid.database.core.table;

import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.Locked;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.exceptions.operation.FailedOperationException;
import net.digitalid.database.core.exceptions.operation.FailedUpdateExecutionException;
import net.digitalid.database.core.sql.identifier.SQLName;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class models a database table.
 */
@Immutable
public class Table {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Stores the name of this table.
     */
    private final @Nonnull SQLName name;
    
    /**
     * Returns the name of this table.
     * 
     * @return the name of this table.
     */
    @Pure
    public final @Nonnull SQLName getName() {
        return name;
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Stores the declaration of this table.
     */
    private final @Nonnull Declaration declaration;
    
    /**
     * Returns the declaration of this table.
     * 
     * @return the declaration of this table.
     */
    @Pure
    public final @Nonnull Declaration getDeclaration() {
        return declaration;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new table with the given name and declaration.
     * 
     * @param name the name of the new table.
     * @param declaration the declaration of the new table.
     */
    protected Table(@Nonnull SQLName name, @Nonnull Declaration declaration) {
        this.name = name;
        this.declaration = declaration;
    }
    
    /**
     * Returns a new table with the given name and declaration.
     * 
     * @param name the name of the new table.
     * @param declaration the declaration of the new table.
     * 
     * @return a new table with the given name and declaration.
     */
    @Pure
    public static @Nonnull Table get(@Nonnull SQLName name, @Nonnull Declaration declaration) {
        return new Table(name, declaration);
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
    public final @Nonnull SQLName getName(@Nonnull Site site) {
        return site + name;
    }
    
    /* -------------------------------------------------- Creation and Deletion -------------------------------------------------- */
    
    /**
     * Creates this table on the given site.
     * 
     * @param site the site on which to create this table.
     * 
     * @require !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
     */
    @Locked
    @NonCommitting
    public final void create(@Nonnull Site site) throws FailedOperationException {
        try (@Nonnull Statement statement = Database.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + getName(site) + " (" + declaration.toString() + ", PRIMARY KEY (" + declaration.getPrimaryKeySelection() + ")" + declaration.getForeignKeys(site) + ")");
            if (isSiteSpecific()) { Database.onInsertIgnore(statement, getName(site), declaration.getPrimaryKeyColumnNames().toArray()); }
            declaration.executeAfterCreation(statement, this, site, true, name);
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
    /**
     * Deletes this table on the given site.
     * 
     * @param site the site on which to delete this table.
     * 
     * @require !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
     */
    @Locked
    @NonCommitting
    public final void delete(@Nonnull Site site) throws FailedOperationException {
        try (@Nonnull Statement statement = Database.createStatement()) {
            declaration.executeBeforeDeletion(statement, this, site, true, name);
            if (isSiteSpecific()) { Database.onInsertNotIgnore(statement, getName(site)); }
            statement.executeUpdate("DROP TABLE IF EXISTS " + getName(site));
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
}
