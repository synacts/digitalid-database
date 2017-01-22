package net.digitalid.database.dialect.statement.update;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.statement.SQLTableStatement;
import net.digitalid.database.unit.Unit;

/**
 * This type models an SQL update statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLUpdateStatement extends SQLTableStatement {
    
    /* -------------------------------------------------- Assignments -------------------------------------------------- */
    
    /**
     * Returns the assignments of this update statement.
     */
    @Pure
    public @Nonnull @NonNullableElements @NonEmpty ImmutableList<? extends SQLAssignment> getAssignments();
    
    /* -------------------------------------------------- Where Clause -------------------------------------------------- */
    
    /**
     * Returns an optional where clause.
     */
    @Pure
    public @Nullable SQLBooleanExpression getWhereClause();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("UPDATE ");
        dialect.unparse(getTable(), unit, string);
        string.append(" SET ");
        dialect.unparse(getAssignments(), unit, string);
        
        final @Nullable SQLBooleanExpression whereClause = getWhereClause();
        if (whereClause != null) {
            string.append(" WHERE ");
            dialect.unparse(whereClause, unit, string);
        }
    }
    
}
