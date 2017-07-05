package net.digitalid.database.dialect.statement.select.unordered.simple.join;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.statement.select.unordered.simple.join.constraints.SQLJoinConstraint;
import net.digitalid.database.dialect.statement.select.unordered.simple.sources.SQLSource;

/**
 * This SQL node represents a join clause of an SQL select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLJoinClause extends SQLNode {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this join clause.
     */
    @Pure
    public @Nonnull SQLJoinOperator getOperator();
    
    /* -------------------------------------------------- Left Source -------------------------------------------------- */
    
    /**
     * Returns the left source of this join clause.
     */
    @Pure
    public @Nonnull SQLSource<?> getLeftSource();
    
    /* -------------------------------------------------- Right Source -------------------------------------------------- */
    
    /**
     * Returns the right source of this join clause.
     */
    @Pure
    public @Nonnull SQLSource<?> getRightSource();
    
    /* -------------------------------------------------- Constraint -------------------------------------------------- */
    
    /**
     * Returns an optional constraint.
     */
    @Pure
    public @Nullable SQLJoinConstraint getConstraint();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getLeftSource(), unit, string);
        string.append(" ");
        dialect.unparse(getOperator(), unit, string);
        string.append(" ");
        dialect.unparse(getRightSource(), unit, string);
        final @Nullable SQLJoinConstraint constraint = getConstraint();
        if (constraint != null) { dialect.unparse(constraint, unit, string); }
    }
    
}
