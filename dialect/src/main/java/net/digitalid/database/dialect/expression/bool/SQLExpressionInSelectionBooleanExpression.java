package net.digitalid.database.dialect.expression.bool;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.subject.site.Site;

/**
 * An SQL expression that checks whether a value is in a given selection.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLExpressionInSelectionBooleanExpression extends SQLBooleanExpression {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the number expression which has to be in the given selection.
     */
    @Pure
    public @Nonnull SQLExpression getExpression();
    
    /* -------------------------------------------------- Selection -------------------------------------------------- */
    
    /**
     * Returns the selection in which the expression has to be.
     */
    @Pure
    public @Nonnull SQLSelectStatement getSelection();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("(");
        dialect.unparse(getExpression(), site, string);
        string.append(") IN (");
        dialect.unparse(getSelection(), site, string);
        string.append(")");
    }
    
}
