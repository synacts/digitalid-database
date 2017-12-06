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

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.functional.failable.FailableConsumer;

/**
 * Represents an SQL row that contains columns that are used to fill a prepared statement.
 * 
 * @param <P> The type of the prepared statement.
 */
public class SQLRow<P> {
    
    /* -------------------------------------------------- Flags -------------------------------------------------- */
    /**
     * A flag that indicates whether the number of columns is known or not.
     */
    private final boolean columnCountUnknown;
    
    /* -------------------------------------------------- Columns for Prepared Statement -------------------------------------------------- */
    
    /**
     * The list of columns that are eventually used to fill the prepared statement.
     */
    private final @Nonnull FreezableArrayList<@Nullable SQLPreparedColumn<P, ?>> columns;
    
    /**
     * Makes sure that the number of columns has a given minimum size.
     * If the column count is unknown, we make sure that the columns list is big enough by adding columns with null values until the required minimum size is reached.
     */
    @Impure
    private void ensureColumnExists(int requiredMinSize) {
        Require.that((!columnCountUnknown && columns.size() >= requiredMinSize) || columnCountUnknown);
        
        while (columnCountUnknown && columns.size() <= requiredMinSize) {
            columns.add(null);
        }
    }
    
    /**
     * Sets the given parameter function at the given parameter index into the prepared statement.
     */
    @Impure
    public <T> void setColumnData(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> parameterFunction, @Nullable T parameterValue, @Nonnull Integer columnIndex) {
        ensureColumnExists(columnIndex);
        columns.set(columnIndex, new SQLPreparedColumn<>(parameterFunction, parameterValue));
    }
    
    /* -------------------------------------------------- Prepared Statement -------------------------------------------------- */
    
    /**
     * Adds the previously created columns to the given prepared statement.
     */
    @Pure
    @SuppressWarnings("all") // suppresses null check of nonnull prepared column (after it has been validated with Require) and unchecked casting to the generic parameters.
    public <T> void addColumnsToPreparedStatement(@Nonnull P preparedStatement) throws SQLException {
        int parameterIndex = 1;
        for (@Nullable SQLPreparedColumn<P, ?> sqlPreparedColumn : columns) {
            Require.that(sqlPreparedColumn != null).orThrow("Not all columns have been processed");
            @Nonnull SQLPreparedColumn<P, T> checkedSqlPreparedColumn = (SQLPreparedColumn<P, T>) sqlPreparedColumn; 
            
            checkedSqlPreparedColumn.parameterFunction.consume(new ParameterFunctionData<>(preparedStatement, parameterIndex, checkedSqlPreparedColumn.parameterValue));
            parameterIndex++;
        }
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL row with an unkown amount of columns.
     */
    private SQLRow() {
        this.columns = FreezableArrayList.withNoElements();
        this.columnCountUnknown = true;
    }
    
    /**
     * Creates a new SQL row with a fixed amount of columns.
     */
    private SQLRow(int columnCount) {
        this.columns = FreezableArrayList.withInitialCapacity(columnCount);
        for (int i = 0; i < columnCount; i++) {
            columns.add(null);
        }
        this.columnCountUnknown = false;
    }
    
    /**
     * Returns an SQL row object with a given column count.
     */
    @Pure
    public static <P> @Nonnull SQLRow<P> with(int columnCount) {
        return new SQLRow<>(columnCount);
    }
    
    /**
     * Returns an SQL row object with an unknown amount of columns.
     */ @Pure
    public static <P> @Nonnull SQLRow<P> withUnknownColumnCount() {
        return new SQLRow<>();
    }
    
    /* -------------------------------------------------- Copy -------------------------------------------------- */
    
    /**
     * Copy constructor.
     */
    private SQLRow(@Nonnull SQLRow<P> sqlRow) {
        this.columns = FreezableArrayList.withInitialCapacity(sqlRow.columns.size());
        for (@Nonnull SQLPreparedColumn<P, ?> sqlPreparedColumn : sqlRow.columns) {
            columns.add(sqlPreparedColumn);
        }
        this.columnCountUnknown = sqlRow.columnCountUnknown;
    }
    
    /**
     * Returns a copy of the given SQL row.
     */
    @Pure
    public static <P> @Nonnull SQLRow<P> copy(@Nonnull SQLRow<P> sqlRow) {
        return new SQLRow<>(sqlRow);
    } 
    
}
