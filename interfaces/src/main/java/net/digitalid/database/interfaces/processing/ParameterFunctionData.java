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
package net.digitalid.database.interfaces.processing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.tuples.Triplet;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * The data used by the parameter function to set a value into a prepared statement.
 */
@Immutable
public class ParameterFunctionData<P, T> extends Triplet<@Nonnull P, @Nonnull Integer, @Nonnull T> {
    
    /* -------------------------------------------------- Prepared Statement -------------------------------------------------- */
    
    /**
     * Returns the prepared statement that is going to be set.
     */
    @Pure
    public @Nonnull P getPreparedStatement() {
        return get0();
    }
    
    /* -------------------------------------------------- Parameter Index -------------------------------------------------- */
    
    /**
     * Returns the parameter index at which the value is going to be set into the prepared statement.
     */
    @Pure
    public @Nonnull Integer getParameterIndex() {
        return get1();
    }
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value which is set into the prepared statement.
     */
    @Pure
    public @Nonnull T getValue() {
        return get2();
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    ParameterFunctionData(@Nonnull P statement, @Nonnull Integer parameterIndex, @Nonnull T value) {
        super(statement, parameterIndex, value);
    }
    
}
