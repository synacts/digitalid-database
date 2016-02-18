package net.digitalid.database.dialect.ast.utility;

import javax.annotation.Nonnull;

import net.digitalid.utility.string.iterable.NonNullableElementConverter;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;

/**
 *
 */
public class SQLNodeConverter  implements NonNullableElementConverter<SQLNode<?>> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    private final @Nonnull SQLDialect dialect;
    
    private final @Nonnull Site site;

    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLNodeConverter(@Nonnull SQLDialect dialect, @Nonnull Site site) {
        this.dialect = dialect;
        this.site = site;
    }
    
    public static @Nonnull SQLNodeConverter get(@Nonnull SQLDialect dialect, @Nonnull Site site) {
        return new SQLNodeConverter(dialect, site);
    }
    
    /* -------------------------------------------------- toString -------------------------------------------------- */
    
    // TODO: the ElementConverter should also use the string builder instead of creating a new string everytime.
    @Override
    public @Nonnull String toString(@Nonnull SQLNode<?> element) {
        StringBuilder string = new StringBuilder();
        dialect.transcribe(site, string, element, false);
        return string.toString();
    }
    
}
