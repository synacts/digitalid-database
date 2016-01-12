package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.collections.annotations.size.MaxSize;

/**
 * This class represents an SQL identifier.
 * The subclasses increase the type safety.
 * 
 * @see SQLName
 * @see SQLAlias
 * @see SQLPrefix
 */
@Immutable
public abstract class SQLIdentifier<T> extends SQLNode<T> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    public abstract @Nonnull @MaxSize(63) String getValue();    
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    @Override
    public final void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) {
        string.append("\"").append(getValue()).append("\"");
    }
    
}
