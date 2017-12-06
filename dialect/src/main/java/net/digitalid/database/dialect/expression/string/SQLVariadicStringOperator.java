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
package net.digitalid.database.dialect.expression.string;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.expression.SQLVariadicOperator;

/**
 * This class enumerates the supported variadic string nodes.
 */
@Immutable
public enum SQLVariadicStringOperator implements SQLVariadicOperator {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * This node concatenates the strings.
     */
    CONCAT("CONCAT"),
    
    /**
     * This node returns the greatest string.
     */
    GREATEST("GREATEST"),
    
    /**
     * This node returns the first non-null string.
     */
    COALESCE("COALESCE");
    
    /* -------------------------------------------------- Symbol -------------------------------------------------- */
    
    private final @Nonnull String symbol;
    
    @Pure
    @Override
    public @Nonnull String getSymbol() {
        return symbol;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLVariadicStringOperator(@Nonnull String symbol) {
        this.symbol = symbol;
    }
    
}
