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

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.Positive;

import net.digitalid.database.interfaces.processing.ParameterFunctionData;
import net.digitalid.database.interfaces.processing.SQLStatementProcessing;

/**
 * The data structure that points to a certain column of an SQL statement.
 */
@Deprecated
class SQLStatementEntry<P> {
    
    /**
     * The statement processing object that handles the setting of the rows and columns of an SQL table.
     */
    private final @Nonnull SQLStatementProcessing<P> sqlStatementProcessing;
    
    /**
     * The parameter index indicates the parameter that is set in the prepared statement.
     */
    private final @NonNegative int parameterIndex;
    
    /**
     * Sets the column data of all rows with a given consumer function and a given value..
     */
    @Impure
    public <T> SQLStatementProcessing<P> setColumnDataAllRows(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> function, @Nonnull T value) {
        sqlStatementProcessing.setColumnParameterFunctionInAllRows(function, value, parameterIndex);
        return sqlStatementProcessing;
    }
    
    /**
     * Sets the column data of a specific row with a given consumer function and a given value..
     */
    @Impure
    public <T> SQLStatementProcessing<P> setColumnDataInRow(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> function, @Nonnull T value, @NonNegative int row) {
        sqlStatementProcessing.setColumnParameterFunctionInRow(function, value, parameterIndex, row);
        return sqlStatementProcessing;
    }
    
    /**
     * Creates a new SQL statement entry with a given statement processing implementation and a fixed parameter index.
     */
    SQLStatementEntry(@Nonnull SQLStatementProcessing<P> sqlStatementProcessing, @NonNegative int parameterIndex) {
        this.sqlStatementProcessing = sqlStatementProcessing;
        this.parameterIndex = parameterIndex;
    }
    
    /**
     * Multiplies the rows by a given number.
     */
    @Impure
    public void multiplyRows(@Positive int number) {
        sqlStatementProcessing.multiplyRows(number);
    }
    
    /**
     * Multiplies a specific row by a given number.
     */
    @Impure
    public void multiplyRow(@NonNegative int row, @Positive int number) {
        sqlStatementProcessing.multiplyRow(row, number);
    }
}
