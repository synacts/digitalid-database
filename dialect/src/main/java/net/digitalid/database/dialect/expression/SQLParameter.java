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
package net.digitalid.database.dialect.expression;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.number.SQLNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLStringExpression;

/**
 * An SQL parameter unparses to a question mark.
 */
@Immutable
@GenerateSubclass
public interface SQLParameter extends SQLBooleanExpression, SQLNumberExpression, SQLStringExpression {
    
    /* -------------------------------------------------- Instances -------------------------------------------------- */
    
    /**
     * Stores an instance of the surrounding class.
     */
    public static final @Nonnull SQLParameter INSTANCE = new SQLParameterSubclass();
    
    /**
     * Stores an instance of the surrounding class as a boolean expression.
     */
    public static final @Nonnull SQLBooleanExpression BOOLEAN = INSTANCE;
    
    /**
     * Stores an instance of the surrounding class as a number expression.
     */
    public static final @Nonnull SQLNumberExpression NUMBER = INSTANCE;
    
    /**
     * Stores an instance of the surrounding class as a string expression.
     */
    public static final @Nonnull SQLStringExpression STRING = INSTANCE;
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append("?");
    }
    
}
