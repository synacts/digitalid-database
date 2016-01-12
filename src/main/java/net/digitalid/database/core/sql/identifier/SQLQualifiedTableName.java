package net.digitalid.database.core.sql.identifier;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.collections.annotations.size.MaxSize;
import net.digitalid.utility.collections.annotations.size.MinSize;

/**
 *
 */
public class SQLQualifiedTableName extends SQLIdentifier<SQLQualifiedTableName> {
    
    public final @Nonnull @MinSize(1) String tableName;
    
    public final @Nonnull Site site;
    
    private SQLQualifiedTableName(@Nonnull @MinSize(1) String tableName, @Nonnull Site site) {
        this.tableName = tableName;
        this.site = site;
    }
    
    public static @Nonnull SQLQualifiedTableName get(@Nonnull @MinSize(1) String tableName, @Nonnull Site site) {
        return new SQLQualifiedTableName(tableName, site);
    }

    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        String qualifiedTableName = tableName + "." + site.toString();
        assert qualifiedTableName.length() <= 63;
        return qualifiedTableName;
    }
    
}
