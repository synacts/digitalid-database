package net.digitalid.database.core.sql;

import javax.annotation.Nonnull;
import net.digitalid.database.core.Dialect;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;

/**
 * Description.
 */
public interface SQLNode {
    
    public abstract void transcribe(@Nonnull Dialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string);
    
}
