package net.digitalid.utility.database.table;

import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.configuration.Database;
import net.digitalid.utility.database.declaration.Declaration;
import net.digitalid.utility.database.exceptions.operation.FailedOperationException;
import net.digitalid.utility.database.exceptions.operation.noncommitting.FailedUpdateExecutionException;
import net.digitalid.utility.database.site.Site;

/**
 * This class models a database table.
 */
@Immutable
public abstract class Table {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns whether the given name is valid.
     * 
     * @param name the name to be checked.
     * 
     * @return whether the given name is valid.
     */
    @Pure
    public static boolean isValidName(@Nonnull String name) {
        return name.length() <= 22 && name.startsWith("_") && name.length() > 1 && Database.getConfiguration().isValidIdentifier(name);
    }
    
    /**
     * Stores the name of this table.
     */
    private final @Nonnull @Validated String name;
    
    /**
     * Returns the name of this table.
     * 
     * @return the name of this table.
     */
    @Pure
    public final @Nonnull @Validated String getName() {
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
     * 
     * @require isSiteSpecific() || !declaration.isSiteSpecific() : "If this table is not site-specific, the declaration is neither.";
     */
    protected Table(@Nonnull @Validated String name, @Nonnull Declaration declaration) {
        assert isValidName(name) : "The name is valid.";
        assert isSiteSpecific() || !declaration.isSiteSpecific() : "If this table is not site-specific, the declaration is neither.";
        
        this.name = name;
        this.declaration = declaration;
    }
    
    /* -------------------------------------------------- Site-Specific Name -------------------------------------------------- */
    
    /**
     * Returns whether this table is {@link Site site}-specific.
     * 
     * @return whether this table is {@link Site site}-specific.
     */
    @Pure
    public abstract boolean isSiteSpecific();
    
    /**
     * Returns the name of this table with the prefix of the given site.
     * 
     * @param site the site whose prefix is to be used for the returned name.
     * 
     * @return the name of this table with the prefix of the given site.
     * 
     * @require !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
     */
    @Pure
    public final @Nonnull @Validated String getName(@Nullable Site site) {
        assert !isSiteSpecific() || site != null : "If this table is site-specific, the site may not be null.";
        
        return (site == null ? "" : site) + name;
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
    public final void create(@Nullable Site site) throws FailedOperationException {
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
    public final void delete(@Nullable Site site) throws FailedOperationException {
        try (@Nonnull Statement statement = Database.createStatement()) {
            declaration.executeBeforeDeletion(statement, this, site, true, name);
            if (isSiteSpecific()) { Database.onInsertNotIgnore(statement, getName(site)); }
            statement.executeUpdate("DROP TABLE IF EXISTS " + getName(site));
        } catch (@Nonnull SQLException exception) {
            throw FailedUpdateExecutionException.get(exception);
        }
    }
    
}
