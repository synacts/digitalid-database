package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

/**
 * This SQL node represents the where clause part of an SQL select or insert statement.
 */
public class SQLWhereClause implements SQLParameterizableNode<SQLWhereClause> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The where clause expression.
     */
    public final @Nonnull SQLBooleanExpression booleanExpression;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new where clause node with a given boolean expression.
     */
    private SQLWhereClause(@Nonnull SQLBooleanExpression booleanExpression) {
        this.booleanExpression = booleanExpression;
    }
    
    /**
     * Returns a new where clause node with a given boolean expression.
     */
    @Pure
    public static @Nonnull SQLWhereClause get(@Nonnull SQLBooleanExpression booleanExpression) {
        return new SQLWhereClause(booleanExpression);
    }
    
    /* -------------------------------------------------- SQL Parameterized Node -------------------------------------------------- */
    
    @Pure
    @Override 
    public void storeValues(@Nonnull @NonCaptured @Modified SQLValueCollector collector) throws FailedSQLValueConversionException {
        booleanExpression.storeValues(collector);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLWhereClause> transcriber = new Transcriber<SQLWhereClause>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLWhereClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(" WHERE ");
            string.append(dialect.transcribe(site, node.booleanExpression));
            return string.toString();
        }
    
    };
    
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLWhereClause> getTranscriber() {
        return transcriber;
    }
    
}
