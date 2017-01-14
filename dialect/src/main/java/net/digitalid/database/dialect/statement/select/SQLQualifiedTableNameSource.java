package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLEncoder;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents a table source for the SQL select statement.
 */
public class SQLQualifiedTableNameSource implements SQLSource<SQLQualifiedTableNameSource> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified table name.
     */
    public final @Nonnull SQLQualifiedTable qualifiedTableName;
    
    /**
     * The optional alias of the table.
     */
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new qualified table name source node with a given qualified table name and an optional alias.
     */
    private SQLQualifiedTableNameSource(@Nonnull SQLQualifiedTable qualifiedTableName, @Nullable String alias) {
        this.qualifiedTableName = qualifiedTableName;
        this.alias = alias;
    }
    
    /**
     * Returns a qualified table name source node with a given qualified table name and an optional alias.
     */
    @Pure
    public static @Nonnull SQLQualifiedTableNameSource get(@Nonnull SQLQualifiedTable qualifiedTableName, @Nullable String alias) {
        return new SQLQualifiedTableNameSource(qualifiedTableName, alias);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLQualifiedTableNameSource> transcriber = new Transcriber<SQLQualifiedTableNameSource>() {
    
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLQualifiedTableNameSource node, @Nonnull Site site) throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.qualifiedTableName));
            if (node.alias != null) {
                string.append(" AS ");
                string.append(node.alias);
            }
            return string.toString();
        }
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLQualifiedTableNameSource> getTranscriber() {
        return transcriber;
    }
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLEncoder collector) throws FailedSQLValueConversionException {
        // intentionally empty
    }
    
}
