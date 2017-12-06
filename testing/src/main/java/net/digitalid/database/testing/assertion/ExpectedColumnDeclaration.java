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
package net.digitalid.database.testing.assertion;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public interface ExpectedColumnDeclaration {
    
    @Impure
    abstract void setFound(boolean found);

    @Pure
    abstract boolean isFound();

    @Pure
    public abstract @Nonnull String getColumnName();

    @Pure
    public abstract @Nonnull String getDBType();

    @Pure
    @Default("true")
    public abstract boolean isNullAllowed();

    @Pure
    @Default("\"\"")
    public abstract @Nonnull String getKey();

    @Pure
    @Default("\"NULL\"")
    public abstract @Nonnull String getDefaultValue();
    
    @Pure
    @Default("null")
    public abstract @Nullable String getColumnConstraint();
}
