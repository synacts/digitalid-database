package net.digitalid.database.dialect.ast.utility.binary;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
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
    public static void transcribeNode(@Nonnull SQLBinaryExpression<?, ?> sqlBinaryExpression, @Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        string.append("(");
        final @Nonnull SQLExpression leftNode = sqlBinaryExpression.getLeftExpression();
        leftNode.getTranscriber().transcribeNode(dialect, leftNode, site, string);
        string.append(") ");
        final @Nonnull SQLBinaryOperator sqlBinaryOperator = sqlBinaryExpression.getOperator();
        sqlBinaryOperator.getTranscriber().transcribeNode(dialect, sqlBinaryOperator, site, string);
        string.append(" (");
        final @Nonnull SQLExpression rightNode = sqlBinaryExpression.getRightExpression();
        rightNode.getTranscriber().transcribeNode(dialect, rightNode, site, string);
        string.append(")");
    }
}
