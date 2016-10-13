package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.interfaces.Site;

/**
 * This SQL node represents the join definition in an SQL select statement.
 */
public class SQLJoinDefinition implements SQLParameterizableNode<SQLJoinDefinition> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The join operator.
     */
    public final @Nonnull SQLJoinOperator joinOperator;
    
    /**
     * The source for the rows that are joined.
     */
    public final @Nonnull SQLSource<?> source;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new join definition node.
     */
    private SQLJoinDefinition(@Nonnull SQLJoinOperator joinOperator, @Nonnull SQLSource<?> source) {
        this.joinOperator = joinOperator;
        this.source = source;
    }
    
    /**
     * Returns a new join definition node.
     */
    @Pure
    public static @Nonnull SQLJoinDefinition get(@Nonnull SQLJoinOperator joinOperator, @Nonnull SQLSource<?> source) {
        return new SQLJoinDefinition(joinOperator, source);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLJoinDefinition> transcriber = new Transcriber<SQLJoinDefinition>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLJoinDefinition node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.joinOperator));
            string.append(" ");
            string.append(dialect.transcribe(site, node.joinOperator));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLJoinDefinition> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        source.storeValues(collector);
    }
}
