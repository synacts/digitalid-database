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
package net.digitalid.database.dialect.statement.select.unordered.compound;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This SQL node represents an SQL compound operator used in an SQL select statement.
 */
@Immutable
public enum SQLCompoundOperator implements SQLBinaryOperator {
    
    /**
     * Combines the results of two select statements.
     */
    UNION("UNION"),
    
    /**
     * Intersects the results of two select statements.
     */
    INTERSECT("INTERSECT"),
    
    /**
     * Excludes the results of the second from the first select statement.
     */
    EXCEPT("EXCEPT");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLCompoundOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
