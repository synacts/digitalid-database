package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.utility.SQLNodeConverter;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This SQL node represents a join clause of an SQL select statement.
 */
public class SQLJoinClause implements SQLSource<SQLJoinClause> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The left source of the join clause.
     */
    public final @Nonnull SQLSource<?> source;
    
    /**
     * The join-operators and right sources or the join clause.
     */
    public final @Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLJoinDefinition> joinDefinitions;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a join clause node.
     */
    private SQLJoinClause(@Nonnull SQLSource<?> source, @Nonnull ReadOnlyList<SQLJoinDefinition> joinDefinitions) {
        this.source = source;
        this.joinDefinitions = joinDefinitions;
    }
    
    /**
     * Returns a join clause node.
     */
    public static @Nonnull SQLJoinClause get(@Nonnull SQLSource<?> source, @Nonnull ReadOnlyList<SQLJoinDefinition> joinDefinitions) {
        return new SQLJoinClause(source, joinDefinitions);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLJoinClause> transcriber = new Transcriber<SQLJoinClause>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLJoinClause node, @Nonnull Site site)  throws InternalException {
            dialect.transcribe(site, string, node.source, parameterizable);
            string.append(" ");
            string.append(IterableConverter.toString(node.joinDefinitions, SQLNodeConverter.get(dialect, site)));
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLJoinClause> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        source.storeValues(collector);
        for (@Nonnull SQLJoinDefinition joinDefinition : joinDefinitions) {
            joinDefinition.storeValues(collector);
        }
    }
}
