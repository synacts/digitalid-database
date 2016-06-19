package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This SQL node represents a table source for the SQL select statement.
 */
public class SQLQualifiedTableNameSource implements SQLSource<SQLQualifiedTableNameSource> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified table name.
     */
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /**
     * The optional alias of the table.
     */
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new qualified table name source node with a given qualified table name and an optional alias.
     */
    private SQLQualifiedTableNameSource(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nullable String alias) {
        this.qualifiedTableName = qualifiedTableName;
        this.alias = alias;
    }
    
    /**
     * Returns a qualified table name source node with a given qualified table name and an optional alias.
     */
    public static @Nonnull SQLQualifiedTableNameSource get(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nullable String alias) {
        return new SQLQualifiedTableNameSource(qualifiedTableName, alias);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLQualifiedTableNameSource> transcriber = new Transcriber<SQLQualifiedTableNameSource>() {
    
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLQualifiedTableNameSource node, @Nonnull Site site, @Nonnull @NonCaptured StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.qualifiedTableName, parameterizable);
            if (node.alias != null) {
                string.append(" AS ");
                string.append(node.alias);
            }
        }
    };
            
    @Override
    public @Nonnull Transcriber<SQLQualifiedTableNameSource> getTranscriber() {
        return transcriber;
    }
    
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        // intentionally empty
    }
    
}
