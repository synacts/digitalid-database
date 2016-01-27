package net.digitalid.database.dialect.ast.utility.variadic;

import javax.annotation.Nonnull;

import net.digitalid.utility.exceptions.internal.InternalException;
import net.digitalid.utility.validation.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.dialect.ast.expression.SQLVariadicExpression;
import net.digitalid.database.dialect.ast.expression.SQLVariadicOperator;

/**
 * Helper class to transcribe variadic expressions.
 */
public final class SQLVariadicExpressionTranscriber {
    
    /**
     * Transcribes binary expressions and stores them in the given string.
     */
    public static void transcribeNode(@Nonnull SQLVariadicExpression<?, ?> node, @Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        final @Nonnull SQLVariadicOperator operator = node.getOperator();
        operator.getTranscriber().transcribeNode(dialect, operator, site, string);
        string.append("(");
        boolean first = true;
        for (final @Nonnull SQLExpression expression : node.getExpressions()) {
            if (!first) { string.append(", "); } else { first = false; }
            expression.getTranscriber().transcribeNode(dialect, expression, site, string);
        }
        string.append(")");
    }
    
}
