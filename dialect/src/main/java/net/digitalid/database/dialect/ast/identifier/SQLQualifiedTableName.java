package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public class SQLQualifiedTableName implements SQLIdentifier<SQLQualifiedTableName> {
    
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
        @Nonnull String qualifiedTableName = site.toString() + "." + tableName;
        assert qualifiedTableName.length() <= 63;
        return qualifiedTableName;
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static final @Nonnull Transcriber<SQLQualifiedTableName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Override
    public @Nonnull
    Transcriber<SQLQualifiedTableName> getTranscriber() {
        return transcriber;
    }
    
}
