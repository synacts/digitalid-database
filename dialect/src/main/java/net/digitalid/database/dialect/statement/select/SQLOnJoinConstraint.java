package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.subject.site.Site;

/**
 * A join constraint based on the given expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLOnJoinConstraint extends SQLJoinConstraint {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression of this join constraint.
     */
    @Pure
    public @Nonnull SQLBooleanExpression getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(" ON ");
        dialect.unparse(getExpression(), site, string);
    }
    
}
