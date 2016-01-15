package net.digitalid.database.core.sql;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;

import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.table.Site;

/**
 * All SQL syntax tree nodes implement this interface.
 */
public abstract class SQLNode<N> extends Transcriber<N> {
    
    /**
     * Transcribes this node to the given dialect at the given site into the given string.
     * 
     * @param dialect the dialect whose implementation is used to transcribe this node.
     * @param site the site at which the SQL statement gets encoded and then executed.
     * @param string the string builder used to encode the SQL statement of this node.
     */
    public abstract void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException;

    protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull N node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
        transcribe(dialect, site, string);
    }
    
}
