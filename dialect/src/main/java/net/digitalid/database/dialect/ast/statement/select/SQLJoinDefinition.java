package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

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
    public static @Nonnull SQLJoinDefinition get(@Nonnull SQLJoinOperator joinOperator, @Nonnull SQLSource<?> source) {
        return new SQLJoinDefinition(joinOperator, source);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLJoinDefinition> transcriber = new Transcriber<SQLJoinDefinition>() {
    
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLJoinDefinition node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.joinOperator, parameterizable);
            string.append(" ");
            dialect.transcribe(site, string, node.joinOperator, parameterizable);
        }
        
    };
    
    @Override 
    public @Nonnull Transcriber<SQLJoinDefinition> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- SQL Parameterizable Node -------------------------------------------------- */
    
    @Override 
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        source.storeValues(collector);
    }
}
