package net.digitalid.database.dialect.expression.number;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.identifier.column.SQLColumn;
import net.digitalid.database.subject.site.Site;

/**
 * An aggregate number expression.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLAggregateNumberExpression extends SQLNumberExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this aggregate expression.
     */
    @Pure
    public @Nonnull SQLAggregateOperator getOperator();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the column of this aggregate expression.
     */
    @Pure
    public @Nonnull SQLColumn getColumn();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Site<?> site, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getOperator(), site, string);
        string.append("(");
        dialect.unparse(getColumn(), site, string);
        string.append(")");
    }
    
}
