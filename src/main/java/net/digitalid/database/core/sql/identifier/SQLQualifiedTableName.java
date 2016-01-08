package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 *
 */
public class SQLQualifiedTableName extends SQLNode<SQLQualifiedTableName> {
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        
    }
}
