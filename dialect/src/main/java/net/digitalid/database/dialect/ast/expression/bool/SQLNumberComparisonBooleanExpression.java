package net.digitalid.database.dialect.ast.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.interfaces.SQLValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLBinaryExpression;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.ast.utility.binary.SQLBinaryExpressionTranscriber;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;


/**
 * Description.
 */
public final class SQLNumberComparisonBooleanExpression extends SQLBooleanExpression implements SQLBinaryExpression<SQLComparisonOperator, SQLNumberExpression<?>> {
    
    private final @Nonnull SQLComparisonOperator operator;
    
    @Override
    public @Nonnull SQLComparisonOperator getOperator() {
        return operator;
    }
    
    private final @Nonnull SQLNumberExpression<?> leftExpression;
    
    @Override
    public @Nonnull SQLNumberExpression<?> getLeftExpression() {
        return leftExpression;
    }
    
    private final @Nonnull SQLNumberExpression<?> rightExpression;
    
    @Override
    public @Nonnull SQLNumberExpression<?> getRightExpression() {
        return rightExpression;
    }
    
    private SQLNumberComparisonBooleanExpression(@Nonnull SQLComparisonOperator operator, @Nonnull SQLNumberExpression<?> leftExpression, @Nonnull SQLNumberExpression<?> rightExpression) {
        this.operator = operator;
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }
    
    public static @Nonnull SQLNumberComparisonBooleanExpression get(@Nonnull SQLComparisonOperator operator, @Nonnull SQLNumberExpression<?> leftExpression, @Nonnull SQLNumberExpression<?> rightExpression) {
        return new SQLNumberComparisonBooleanExpression(operator, leftExpression, rightExpression);
    }
    
    /* -------------------------------------------------- SQLParameterizableNode -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        leftExpression.storeValues(collector);
        rightExpression.storeValues(collector);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLNumberComparisonBooleanExpression> transcriber = new Transcriber<SQLNumberComparisonBooleanExpression>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLNumberComparisonBooleanExpression node, @Nonnull Site site)  throws InternalException {
            return SQLBinaryExpressionTranscriber.transcribeNode(node, dialect, site);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLNumberComparisonBooleanExpression> getTranscriber() {
        return transcriber;
    }
    
}
