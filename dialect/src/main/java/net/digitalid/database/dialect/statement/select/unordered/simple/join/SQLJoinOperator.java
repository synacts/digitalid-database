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
package net.digitalid.database.dialect.statement.select.unordered.simple.join;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLBinaryOperator;

/**
 * This class enumerates the supported join operators.
 */
@Immutable
public enum SQLJoinOperator implements SQLBinaryOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    LEFT("LEFT"),
    LEFT_OUTER("LEFT OUTER"),
    INNER("INNER"),
    CROSS("CROSS"),
    NATURAL_LEFT("NATURAL LEFT"),
    NATURAL_LEFT_OUTER("NATURAL LEFT OUTER"),
    NATURAL_INNER("NATURAL INNER"),
    NATURAL_CROSS("NATURAL CROSS");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLJoinOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
