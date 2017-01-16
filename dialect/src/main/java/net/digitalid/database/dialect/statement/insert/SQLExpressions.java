package net.digitalid.database.dialect.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.subject.site.Site;

/**
 * SQL expressions.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLExpressions extends SQLNode {
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Returns the expressions whose evaluations are to be inserted.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<SQLExpression> getExpressions();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("(");
        dialect.unparse(getExpressions(), site, string);
        string.append(")");
    }
    
}
