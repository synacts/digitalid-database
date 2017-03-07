package net.digitalid.database.dialect.statement.insert;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLOperator;

/**
 * This class models the conflict clause of insert statements.
 * 
 * @see <a href="https://www.sqlite.org/lang_conflict.html">SQLite Documentation</a>
 */
@Immutable
public enum SQLConflictClause implements SQLOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * Rolls back the current transaction in case of a constraint violation and triggers an {@link SQLException}.
     */
    ROLLBACK("INSERT OR ROLLBACK"),
    
    /**
     * Aborts the current SQL statement in case of a constraint violation while leaving earlier statements and the current transaction active and triggers an {@link SQLException}.
     */
    ABORT("INSERT OR ABORT"),
    
    /**
     * Fails the current SQL statement in case of a constraint violation while leaving earlier changes of the same statement and the current transaction active and triggers an {@link SQLException}.
     */
    FAIL("INSERT OR FAIL"),
    
    /**
     * Ignores the rows with a constraint violation without triggering an {@link SQLException} and processes all other rows as if nothing went wrong.
     */
    IGNORE("INSERT OR IGNORE"),
    
    /**
     * Replaces the rows with a unique or primary key constraint violation without triggering an {@link SQLException}.
     */
    REPLACE("INSERT OR REPLACE");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLConflictClause(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
