package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Quotes;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.storage.Site;

/**
 * The SQL node that represents the qualified table name.
 */
public class SQLQualifiedTableName implements SQLNode<SQLQualifiedTableName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The table name.
     */
    public final @Nonnull @MinSize(1) String tableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new qualified table name for a given table name.
     */
    private SQLQualifiedTableName(@Nonnull @MinSize(1) String tableName) {
        this.tableName = tableName;
    }
    
    /**
     * Returns a qualified table name for a given table name.
     */
    @Pure
    public static @Nonnull SQLQualifiedTableName get(@Nonnull @MinSize(1) String tableName) {
        return new SQLQualifiedTableName(tableName);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that return a string representation of this SQL node.
     */
    private static final @Nonnull Transcriber<SQLQualifiedTableName> transcriber = new Transcriber<SQLQualifiedTableName>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLQualifiedTableName node, @Nonnull Site site) throws InternalException {
            final @Nonnull String qualifiedTableName = (site.getDatabaseName().isEmpty() ? "" : site.getDatabaseName() + ".") + node.tableName.length();
            Require.that(qualifiedTableName.length() <= 63).orThrow("The qualified table name $ is bigger than allowed");
            
            return Quotes.inDouble(qualifiedTableName);
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLQualifiedTableName> getTranscriber() {
        return transcriber;
    }
    
}
