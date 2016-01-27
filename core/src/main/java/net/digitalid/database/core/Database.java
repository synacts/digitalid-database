package net.digitalid.database.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Initialized;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Stateless;

import net.digitalid.database.core.annotations.Committing;
import net.digitalid.database.exceptions.operation.FailedCommitException;
import net.digitalid.database.core.interfaces.DatabaseInstance;

/**
 * This class provides connections to the database.
 */
@Stateless
public final class Database {
    
    /* -------------------------------------------------- Instance -------------------------------------------------- */
    
    /**
     * Stores the database instance.
     */
    private static @Nullable DatabaseInstance instance;
    
    /**
     * Returns the database instance.
     * <p>
     * <em>Important:</em> Do not store
     * the instance permanently because
     * it may change during testing!
     * 
     * @return the database instance.
     */
    @Pure
    @Initialized
    public static @NonCapturable @Nonnull DatabaseInstance getInstance() {
        assert instance != null : "The database is initialized.";
        
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
     * 
     * @return whether the database is set up for single-access.
     */
    @Pure
    public static boolean isSingleAccess() {
        return singleAccess;
    }
    
    /**
     * Returns whether the database is set up for multi-access.
     * 
     * @return whether the database is set up for multi-access.
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
    public static void initialize(@Nonnull DatabaseInstance instance, boolean singleAccess) {
        Database.instance = instance;
        Database.singleAccess = singleAccess;
        
        Log.information("The database has been initialized for " + (singleAccess ? "single" : "multi") + "-access.");
    }
    
    /**
     * Initializes the database with the given instance and dialect.
     * 
     * @param instance the instance with which the database is configured.
     */
    public static void initialize(@Nonnull DatabaseInstance instance) {
        initialize(instance, true);
    }
    
    /**
     * Returns whether the database has been initialized.
     * 
     * @return whether the database has been initialized.
     */
    @Pure
    public static boolean isInitialized() {
        return instance != null;
    }
    
    /* -------------------------------------------------- Transactions -------------------------------------------------- */
    
    /**
     * Commits all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Committing
    @Initialized
    public static void commit() throws FailedCommitException {
        getInstance().commit();
    }
    
    /**
     * Rolls back all changes of the current thread since the last commit or rollback.
     * (On the server, this method should only be called by the worker.)
     */
    @Committing
    @Initialized
    public static void rollback() {
        getInstance().rollback();
    }
    
}
