package net.digitalid.database.core.sql;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * All SQL syntax tree nodes implement this interface.
 */
public interface SQLNode {
    
    /**
     * Transcribes this node to the given dialect at the given site into the given string.
     * 
     * @param dialect the dialect whose implementation is used to transcribe this node.
     * @param site the site at which the SQL statement gets encoded and then executed.
     * @param string the string builder used to encode the SQL statement of this node.
     */
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException;
    
}
