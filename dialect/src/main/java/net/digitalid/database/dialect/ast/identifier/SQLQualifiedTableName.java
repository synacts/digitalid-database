package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * The SQL node that represents the qualified table name.
 */
public class SQLQualifiedTableName implements SQLIdentifier<SQLQualifiedTableName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The table name.
     */
    public final @Nonnull @MinSize(1) String tableName;
    
    /**
     * The site that qualifies the table name.
     */
    public final @Nonnull Site site;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new qualified table name for a given table name and site.
     */
    private SQLQualifiedTableName(@Nonnull @MinSize(1) String tableName, @Nonnull Site site) {
        this.tableName = tableName;
        this.site = site;
    }
    
    /**
     * Returns a qualified table name for a given table name and site.
     */
    @Pure
    public static @Nonnull SQLQualifiedTableName get(@Nonnull @MinSize(1) String tableName, @Nonnull Site site) {
        return new SQLQualifiedTableName(tableName, site);
    }
    
    @Pure
    @Override
    public @Nonnull @MaxSize(63) String getValue() {
        @Nonnull String qualifiedTableName = site.toString() + "." + tableName;
        Require.that(qualifiedTableName.length() <= 63).orThrow("The qualified name $ is bigger than allowed", qualifiedTableName);
        return qualifiedTableName;
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that return a string representation of this SQL node.
     */
    private static final @Nonnull Transcriber<SQLQualifiedTableName> transcriber = new SQLIdentifierTranscriber<>();
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLQualifiedTableName> getTranscriber() {
        return transcriber;
    }
    
}
