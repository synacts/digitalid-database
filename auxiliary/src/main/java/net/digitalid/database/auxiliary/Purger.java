package net.digitalid.database.auxiliary;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.validation.annotations.type.Stateless;

/**
 * Description.
 */
@Stateless
@TODO(task = "Redo the whole class if it is still useful.", date = "2017-01-18", author = Author.KASPAR_ETTER)
public abstract class Purger {
    
    /* -------------------------------------------------- Purging -------------------------------------------------- */
    
    /**
     * Stores the timer to schedule tasks.
     */
    private static final @Nonnull Timer timer = new Timer();
    
    /**
     * Stores the tables which are to be purged regularly.
     */
    private static final @Nonnull ConcurrentMap<String, Long> tables = new ConcurrentHashMap<>();
    
    /**
     * Adds the given table to the list for regular purging.
     * 
     * @param table the name of the table which is to be purged regularly.
     * @param time the time after which entries in the given table can be purged.
     */
    @Impure
    public static void addRegularPurging(@Nonnull String table, long time) {
        tables.put(table, time);
    }
    
    /**
     * Removes the given table from the list for regular purging.
     * 
     * @param table the name of the table which is no longer to be purged.
     */
    @Impure
    public static void removeRegularPurging(@Nonnull String table) {
        tables.remove(table);
    }
    
    /**
     * Starts the timer for purging.
     */
    @Impure
    public static void startPurging() {
        // TODO: If several processes access the database, it's enough when one of them does the purging.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO: reimplement in Converter.
/*                try {
                    try (@Nonnull Statement statement = createStatement()) {
                        final long time = System.currentTimeMillis();
                        for (final @Nonnull Map.Entry<String, Long> entry : tables.entrySet()) {
                            statement.executeUpdate("DELETE FROM " + entry.getKey() + " WHERE time < " + (time - entry.getValue()));
                            commit();
                        }
                    }
                } catch (@Nonnull FailedOperationException | SQLException exception) {
                    Log.warning("Could not prune a table.", exception);
                    rollback();
                }
*/
            }
        }, 60_000l, 3_600_000l);
    }
    
    /**
     * Stops the timer for purging.
     */
    @Impure
    public static void stopPurging() {
        timer.cancel();
    }
    
}
