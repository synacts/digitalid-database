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

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.expression.number.SQLVariadicNumberExpression;
import net.digitalid.database.dialect.expression.string.SQLVariadicStringExpression;

/**
 * All variadic expressions implement this interface.
 * 
 * @see SQLVariadicNumberExpression
 * @see SQLVariadicStringExpression
 */
@Immutable
public interface SQLVariadicExpression<@Unspecifiable OPERATOR extends SQLVariadicOperator, @Unspecifiable EXPRESSION extends SQLExpression> extends SQLExpression {
    
    /* -------------------------------------------------- Operator -------------------------------------------------- */
    
    /**
     * Returns the operator of this variadic expression.
     */
    @Pure
    public @Nonnull OPERATOR getOperator();
    
    /* -------------------------------------------------- Expressions -------------------------------------------------- */
    
    /**
     * Returns the child expressions of this variadic expression.
     */
    @Pure
    public @Nonnull @NonNullableElements ImmutableList<? extends EXPRESSION> getExpressions();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getOperator(), unit, string);
        string.append("(");
        dialect.unparse(getExpressions(), unit, string);
        string.append(")");
    }
    
}
