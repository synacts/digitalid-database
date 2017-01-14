package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCapturable;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.transaction.Committing;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * This class provides connections to the database.
 */
@Utility
@Deprecated // TODO: Use configuration fields in the database interface instead.
public abstract class DatabaseUtility {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    /**
     * Stores the database instance.
     */
    private static @Nullable Database instance;
    
    /**
     * Returns whether the database has been initialized.
     */
    @Pure
    public static boolean isInitialized() {
        return instance != null;
    }
    
    /**
     * Returns the database instance.
     * <p>
     * <em>Important:</em> Do not store the instance permanently because it may change during testing!
     */
    @Pure
    @SuppressWarnings("null")
    public static @NonCapturable @Nonnull Database getInstance() {
        Require.that(instance != null).orThrow("The database has to be initialized.");
        
        return instance;
    }
    
    /* -------------------------------------------------- Single-Access -------------------------------------------------- */
    
    /**
     * Stores whether the database is set up for single-access.
     * In case of single-access, only one process accesses the
     * database, which allows to keep the objects in memory up
     * to date with no need to reload them all the time.
     * (Clients on hosts are run in multi-access mode.)
     */
    private static boolean singleAccess;
    
    /**
     * Returns whether the database is set up for single-access.
     */
    @Pure
    public static boolean isSingleAccess() {
        return singleAccess;
    }
    
    /**
     * Returns whether the database is set up for multi-access.
     */
    @Pure
    public static boolean isMultiAccess() {
        return !singleAccess;
    }
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the database with the given instance and dialect.
     * 
     * @param instance the instance with which the database is configured.
     * @param singleAccess whether the database is accessed by a single process.
     */
    @Impure
    public static void initialize(@Nonnull Database instance, boolean singleAccess) {
        DatabaseUtility.instance = instance;
        DatabaseUtility.singleAccess = singleAccess;
        
        Log.information("The database has been initialized for " + (singleAccess ? "single" : "multi") + "-access.");
    }
    
    /**
     * Initializes the database with the given instance and dialect.
     * 
     * @param instance the instance with which the database is configured.
     */
    @Impure
    public static void initialize(@Nonnull Database instance) {
        initialize(instance, true);
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Commits all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Impure
    @Committing
    public static void commit() throws DatabaseException {
        getInstance().commit();
    }
    
    /**
     * Rolls back all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Impure
    @Committing
    public static void rollback() {
        getInstance().rollback();
    }
    
}
