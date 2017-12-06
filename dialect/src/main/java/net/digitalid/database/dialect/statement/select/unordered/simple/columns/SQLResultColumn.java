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
package net.digitalid.database.dialect.statement.select.unordered.simple.columns;

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
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnAlias;

/**
 * This SQL node represents a result column in a select statement.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLResultColumn extends SQLResultColumnOrAllColumns {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * The qualified column name.
     */
    @Pure
    public @Nonnull SQLExpression getExpression();
    
    /* -------------------------------------------------- Alias -------------------------------------------------- */
    
    /**
     * The alias of the qualified column name.
     */
    @Pure
    public @Nullable SQLColumnAlias getAlias();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getExpression(), unit, string);
        final @Nullable SQLColumnAlias alias = getAlias();
        if (alias != null) {
            string.append(" AS ");
            dialect.unparse(alias, unit, string);
        }
    }
    
}
