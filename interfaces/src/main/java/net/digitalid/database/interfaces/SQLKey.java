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
package net.digitalid.database.interfaces;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.model.CustomType;

/**
 *
 */
@Deprecated
public class SQLKey {
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    private final @Nonnull CustomType type;
    
    @Pure
    public @Nonnull CustomType getType() {
        return type;
    }
    
    /* -------------------------------------------------- Column Name -------------------------------------------------- */
    
    private final @Nonnull String columnName;
    
    @Pure
    public @Nonnull String getColumnName() {
        return columnName;
    }
    
    /* -------------------------------------------------- Column Position -------------------------------------------------- */
    
    public final int columnPosition;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLKey(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        this.type = type;
        this.columnName = columnName;
        this.columnPosition = columnPosition;
    }
    
    @Pure
    public static @Nonnull SQLKey with(@Nonnull CustomType type, @Nonnull String columnName, int columnPosition) {
        return new SQLKey(type, columnName, columnPosition);
    }
    
}
