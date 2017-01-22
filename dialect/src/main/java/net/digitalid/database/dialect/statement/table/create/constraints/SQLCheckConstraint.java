package net.digitalid.database.dialect.statement.table.create.constraints;

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
import net.digitalid.database.unit.Unit;

/**
 * A check constraint in a create table statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLCheckConstraint extends SQLTableConstraint {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression of this check constraint.
     */
    @Pure
    public @Nonnull SQLBooleanExpression getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        SQLTableConstraint.super.unparse(dialect, unit, string);
        string.append(" CHECK (");
        dialect.unparse(getExpression(), unit, string);
        string.append(")");
    }
    
}
