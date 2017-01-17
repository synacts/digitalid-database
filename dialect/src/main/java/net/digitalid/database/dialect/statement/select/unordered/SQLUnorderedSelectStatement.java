package net.digitalid.database.dialect.statement.select.unordered;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.compound.SQLCompoundSelectStatement;

/**
 * An SQL select statement without an order or limit clause.
 * 
 * @see SQLSimpleSelectStatement
 * @see SQLCompoundSelectStatement
 */
@Immutable
public interface SQLUnorderedSelectStatement extends SQLSelectStatement {
    
    /* -------------------------------------------------- Compound Operations -------------------------------------------------- */
    
    /**
     * Combines the results of this and the given select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement union(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return todo;
    }
    
    /**
     * Intersects the results of this and the given select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement intersect(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return todo;
    }
    
    /**
     * Excludes the results of given select statement from this select statement.
     */
    @Pure
    public default @Nonnull SQLCompoundSelectStatement except(@Nonnull SQLUnorderedSelectStatement selectStatement) {
        return todo;
    }
    
}
