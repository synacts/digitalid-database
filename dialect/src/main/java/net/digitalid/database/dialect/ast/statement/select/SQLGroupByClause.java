package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.utility.SQLNodeConverter;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;

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
    public static @Nonnull SQLGroupByClause get(@Nonnull @MinSize(1) @NonNullableElements ReadOnlyList<SQLQualifiedColumnName> qualifiedColumnNames, @Nullable SQLBooleanExpression havingExpression) {
        return new SQLGroupByClause(qualifiedColumnNames, havingExpression);
    }
    
    /* -------------------------------------------------- SQL Parameterized Node -------------------------------------------------- */
    
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
            string.append(" GROUP BY ");
            string.append(IterableConverter.toString(node.qualifiedColumnNames, SQLNodeConverter.get(dialect, site)));
            if (node.havingExpression != null) {
                string.append(" HAVING ");
                dialect.transcribe(site, string, node.havingExpression, parameterizable);
            }
        }
    
    };
    
    @Override 
    public @Nonnull Transcriber<SQLGroupByClause> getTranscriber() {
        return transcriber;
    }
    
}
