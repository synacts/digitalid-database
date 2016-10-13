package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.interfaces.Site;

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
    @Pure
    public static @Nonnull SQLJoinClause get(@Nonnull SQLSource<?> source, @Nonnull ReadOnlyList<SQLJoinDefinition> joinDefinitions) {
        return new SQLJoinClause(source, joinDefinitions);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that returns a string representation of this SQL node.
     */
    private static final @Nonnull Transcriber<SQLJoinClause> transcriber = new Transcriber<SQLJoinClause>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLJoinClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.source));
            string.append(" ");
            string.append(node.joinDefinitions.map(joinDefinition -> dialect.transcribe(site, joinDefinition)).join());
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLJoinClause> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        source.storeValues(collector);
        for (@Nonnull SQLJoinDefinition joinDefinition : joinDefinitions) {
            joinDefinition.storeValues(collector);
        }
    }
}
