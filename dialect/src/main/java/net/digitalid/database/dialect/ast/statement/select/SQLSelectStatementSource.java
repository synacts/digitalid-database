package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.subject.Site;

/**
 * This SQL node represents a select statement source for another SQL select statement.
 */
public class SQLSelectStatementSource implements SQLSource<SQLSelectStatementSource> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The select statement node of this source node.
     */
    public final @Nonnull SQLSelectStatement selectStatement;
    
    /**
     * The optional alias of this source node.
     */
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new select statement source with a given select statement and an optional alias.
     */
    private SQLSelectStatementSource(@Nonnull SQLSelectStatement selectStatement, @Nullable String alias) {
        this.selectStatement = selectStatement;
        this.alias = alias;
    }
    
    /**
     * Returns a new select statement source with a given select statement and an optional alias.
     */
    @Pure
    public static @Nonnull SQLSelectStatementSource get(@Nonnull SQLSelectStatement sqlSelectStatement, @Nullable String alias) {
        return new SQLSelectStatementSource(sqlSelectStatement, alias);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLSelectStatementSource> transcriber = new Transcriber<SQLSelectStatementSource>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLSelectStatementSource node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            dialect.transcribe(site, node.selectStatement);
            if (node.alias != null) {
                string.append(" AS ");
                string.append(node.alias);
            }
            return string.toString();
        }
    
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLSelectStatementSource> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        selectStatement.storeValues(collector);
    }
    
}
