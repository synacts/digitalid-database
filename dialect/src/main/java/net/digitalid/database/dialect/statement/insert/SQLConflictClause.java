/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
