package net.digitalid.database.access;

import net.digitalid.utility.validation.annotations.type.Stateless;

/**
 * The mode determines whether the database is accessed by a single process.
 */
@Stateless
public enum Mode {
    
    /**
     * In case of single-access, only one process accesses the
     * database, which allows to keep the objects in memory up
     * to date with no need to reload them all the time.
     */
    SINGLE,
    
    /**
     * Several processes access the database (for load balancing).
     * (Clients on hosts are run in multi-access mode.)
     */
    MULTI;
    
}
