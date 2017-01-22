package net.digitalid.database.dialect.statement.update;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.unit.Unit;

/**
 * This type models an assignment in an update statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLAssignment extends SQLNode {
    
    /* -------------------------------------------------- Column -------------------------------------------------- */
    
    /**
     * Returns the column to which the given expression is assigned.
     */
    @Pure
    public @Nonnull SQLColumnName getColumn();
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Returns the expression that is assigned to the given column.
     */
    @Pure
    public @Nonnull SQLExpression getExpression();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getColumn(), unit, string);
        string.append(" = ");
        dialect.unparse(getExpression(), unit, string);
    }
    
}
