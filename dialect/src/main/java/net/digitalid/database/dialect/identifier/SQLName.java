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
package net.digitalid.database.dialect.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.table.SQLTableName;

/**
 * An SQL name.
 * 
 * @see SQLColumnName
 * @see SQLTableName
 * @see SQLSchemaName
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLName extends SQLIdentifier {
    
    /* -------------------------------------------------- Prefixing -------------------------------------------------- */
    
    /**
     * Returns this name prefixed with the given prefix.
     * 
     * @require prefix.getString().length() + getString().length() <= 62 : "The added lengths of the prefix and this name is at most 62.";
     */
    @Pure
    public default @Nonnull SQLName prefixedWith(@Nonnull SQLPrefix prefix) {
        Require.that(prefix.getString().length() + getString().length() <= 62).orThrow("The added lengths of the prefix $ and this name $ may be at most 62.", prefix.getString(), getString());
        
        return SQLNameBuilder.withString(prefix.getString() + "_" + this.getString()).build();
    }
    
}
