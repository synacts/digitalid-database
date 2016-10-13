package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.interfaces.Site;

/**
 * This SQL node represents a GROUP BY clause for an SQL select statement.
 */
public class SQLGroupByClause implements SQLParameterizableNode<SQLGroupByClause> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The list of qualifiedColumnNames used in the group clause.
     */
    public final @Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames;
    
    /**
     * The HAVING boolean expression.
     */
    public final @Nullable SQLBooleanExpression havingExpression;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a group-by clause node.
     */
    private SQLGroupByClause(@Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames, @Nullable SQLBooleanExpression havingExpression) {
        this.qualifiedColumnNames = qualifiedColumnNames;
        this.havingExpression = havingExpression;
    }
    
    /**
     * Returns a group-by clause node.
     */
    @Pure
    public static @Nonnull SQLGroupByClause get(@Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames, @Nullable SQLBooleanExpression havingExpression) {
        return new SQLGroupByClause(qualifiedColumnNames, havingExpression);
    }
    
    /* -------------------------------------------------- SQL Parameterized Node -------------------------------------------------- */
    
    @Pure
    @Override 
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        if (havingExpression != null) {
            havingExpression.storeValues(collector);
        }
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLGroupByClause> transcriber = new Transcriber<SQLGroupByClause>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLGroupByClause node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(" GROUP BY ");
            string.append(node.qualifiedColumnNames.map(columnName -> dialect.transcribe(site, columnName)).join());
            if (node.havingExpression != null) {
                string.append(" HAVING ");
                dialect.transcribe(site, node.havingExpression);
            }
            return string.toString();
        }
    
    };
    
    @Pure
    @Override 
    public @Nonnull Transcriber<SQLGroupByClause> getTranscriber() {
        return transcriber;
    }
    
}
