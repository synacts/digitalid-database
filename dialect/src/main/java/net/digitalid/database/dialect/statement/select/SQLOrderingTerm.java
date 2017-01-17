package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents the ordering term of an SQL select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLOrderingTerm extends SQLNode {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression according to which the rows are ordered.
     */
    @Pure
    public @Nonnull SQLExpression getExpression();
    
    /* -------------------------------------------------- Direction -------------------------------------------------- */
    
    /**
     * Returns whether the ordering is ascending.
     */
    @Pure
    @Default("true")
    public boolean isAscending();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getExpression(), site, string);
        string.append(isAscending() ? " ASC" : " DESC");
    }
    
}
