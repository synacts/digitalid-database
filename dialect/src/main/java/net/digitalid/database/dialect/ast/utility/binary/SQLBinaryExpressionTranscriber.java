package net.digitalid.database.dialect.ast.utility.binary;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.storage.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLBinaryExpression;
import net.digitalid.database.dialect.ast.expression.SQLBinaryOperator;
import net.digitalid.database.dialect.ast.expression.SQLExpression;

/**
 * Helper class to transcribe binary expressions.
 */
public final class SQLBinaryExpressionTranscriber {
    
    /**
     * Transcribes binary expressions and stores them in the given string.
     */
    @Pure
    public static String transcribeNode(@Nonnull SQLBinaryExpression<?, ?> sqlBinaryExpression, @Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder string = new StringBuilder();
        string.append("(");
        final @Nonnull SQLExpression leftNode = sqlBinaryExpression.getLeftExpression();
        string.append(dialect.transcribe(site, leftNode));
        string.append(") ");
        final @Nonnull SQLBinaryOperator sqlBinaryOperator = sqlBinaryExpression.getOperator();
        string.append(dialect.transcribe(site, sqlBinaryOperator));
        string.append(" (");
        final @Nonnull SQLExpression rightNode = sqlBinaryExpression.getRightExpression();
        string.append(dialect.transcribe(site, rightNode));
        string.append(")");
        return string.toString();
    }
}
