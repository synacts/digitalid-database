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

import java.sql.SQLException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * Represents an SQL column that is eventually set using the parameter function and the parameter value.
 *
 * @param <P> The prepared-statement that is set.
 * @param <T> The type of the value that is set.
 */
@Immutable
class SQLPreparedColumn<P, T> {
    
    /* -------------------------------------------------- Parameter Function -------------------------------------------------- */
    
    /**
     * The parameter function that sets the value into the actual prepared-statement.
     */
    final @Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, @Nonnull SQLException> parameterFunction;
    
    /* -------------------------------------------------- Parameter Value -------------------------------------------------- */
    
    /**
     * The value that is set into the actual prepared-statement.
     */
    final @Nullable T parameterValue;
    
    /* -------------------------------------------------- Consumer -------------------------------------------------- */
    
    /**
     * Creates a new SQLColumn object with the parameter function and value.
     */
    SQLPreparedColumn(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> parameterFunction, @Nullable T parameterValue) {
        this.parameterFunction = parameterFunction;
        this.parameterValue = parameterValue;
    }
    
}
