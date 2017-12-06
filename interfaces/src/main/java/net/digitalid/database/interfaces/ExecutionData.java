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
import java.util.LinkedList;
import java.util.Stack;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.state.Modifiable;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.tuples.Pair;
import net.digitalid.utility.validation.annotations.math.Positive;

import net.digitalid.database.interfaces.processing.ParameterFunctionData;
import net.digitalid.database.interfaces.processing.SQLStatementProcessing;

/**
 * Stores the data that is collected while processing an SQL statement. Data collected through the value collectors of the object converters must be serialized into SQL. The execution data object provides means to easily insert the data in the correct order and fill and provide driver-independent prepared statements with the data.
 * 
 * @param <P> the type of the prepared statement for driver-independent queries and updates.
 */
@Deprecated
public class ExecutionData<P> {
    
    /* -------------------------------------------------- Prepared Statement Entry Queue -------------------------------------------------- */
    
    /**
     * Stores the current index into the prepared statement entry queue.
     */
    private int currentIndexIntoQueue = 0;
    
    /**
     * A list of prepared statement entries, which correspond to a list of rows and columns of prepared statements, ordered by insertion.
     */
    private final @Nonnull LinkedList<@Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>>> preparedStatementEntryQueue;
    
    /**
     * Polls the next prepared statement entry in line. If a mark has been set, we return the next prepared statement entry, but leave the queue intact such that it can be reset again.
     */
    @Impure
    private @Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>> pollPreparedStatementEntry() {
        if (!marks.isEmpty()) {
            @Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>> preparedStatementEntries = preparedStatementEntryQueue.get(currentIndexIntoQueue);
            currentIndexIntoQueue++;
            return preparedStatementEntries;
        } else {
            columnCountForGroup.poll();
            return preparedStatementEntryQueue.poll();
        }
    }
    
    /* -------------------------------------------------- Marks -------------------------------------------------- */
    
    /**
     * Stores the marks of the queue. If the stack is not empty, entries from the prepared statement entry queue are not dropped when being polled. The marks define to which entry the queue is going to be restored when {@link #resetPreparedStatementQueue()} is called.
     */
    private final Stack<Integer> marks = new Stack<>();
    
    /**
     * Marks the prepared statement queue such that it can be restored to the current state with {@link #resetPreparedStatementQueue()}.
     */
    @Impure
    public void markPreparedStatementQueue() {
        marks.add(currentIndexIntoQueue);
    }
    
    /**
     * Resets the prepared statement queue to the latest mark.
     */
    @Impure
    public void resetPreparedStatementQueue() {
        Require.that(!marks.isEmpty()).orThrow("The prepared statement queue must be marked before it can be reset.");
        
        final @Nonnull Integer mark = marks.pop();
        currentIndexIntoQueue = mark;
    }
    
    /**
     * Removes entries from the prepared statement queue until all entries which belong to the same column group have been dropped.
     */
    @Impure
    public void popPreparedStatementEntriesOfGroup() {
        if (marks.isEmpty()) {
            for (int i = 0; i < columnCountForGroup.peek(); i++) {
                preparedStatementEntryQueue.poll();
            }
            resetRowIndex();
        }
    }
    
    /* -------------------------------------------------- Row Index -------------------------------------------------- */
    
    /**
     * The row index indicates whether only a specific row is going to be modified or, if set to -1, if all rows are modified.
     */
    private int rowIndex = -1;
    
    /**
     * Marks a specific row for further modification.
     */
    @Impure
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    
    /**
     * Resets the row index.
     */
    @Impure
    private void resetRowIndex() {
        this.rowIndex = -1;
    }
    
    /**
     * Returns the current row index.
     */
    @Pure
    public int getCurrentRowIndex() {
        return rowIndex;
    }
    
    /* -------------------------------------------------- Rows Ordered By Execution -------------------------------------------------- */
    
    /**
     * A list of SQL statement processing entries that correspond to the prepared statements ordered by statement execution.
     */
    private @Nonnull @Modifiable FreezableArrayList<@Nonnull ? extends SQLStatementProcessing<P>> rowsOfOrderedStatements;
    
    /**
     * Returns a list of filled prepared statements.
     */
    @Pure
    public @Frozen @Nonnull ReadOnlyList<P> getPreparedStatements() throws SQLException {
        Require.that(preparedStatementEntryQueue.isEmpty()).orThrow("Not all columns have been processed");
        
        final @Nonnull FreezableArrayList<P> preparedStatements = FreezableArrayList.withInitialCapacity(rowsOfOrderedStatements.size());
        for (@Nonnull SQLStatementProcessing<P> SQLStatementProcessing : rowsOfOrderedStatements) {
            SQLStatementProcessing.addRowsToPreparedStatementBatch();
            preparedStatements.add(SQLStatementProcessing.getPreparedStatement());
        }
        return preparedStatements.freeze();
    }
    
    /* -------------------------------------------------- Column Count for Group -------------------------------------------------- */
    
    /**
     * A column count corresponds to the number of columns used for the current field.
     */
    private final @Nonnull LinkedList<@Nonnull Integer> columnCountForGroup;
    
    /* -------------------------------------------------- Multiply Rows -------------------------------------------------- */
    
    /**
     * Multiply rows by a given number. If a row has been marked, only the specific row is multiplies. Otherwise, all rows are multiplied.
     */
    @Impure
    public void multiplyRows(@Positive int number) {
        markPreparedStatementQueue();
        for (@Nonnull SQLStatementEntry<P> SQLStatementEntry : pollPreparedStatementEntry()) {
            if (rowIndex == -1) {
                SQLStatementEntry.multiplyRows(number);
                rowIndex = 0;
            } else {
                SQLStatementEntry.multiplyRow(rowIndex, number);
            }
        }
        resetPreparedStatementQueue();
    }
    
    /* -------------------------------------------------- Column Meta Data -------------------------------------------------- */
    
    /**
     * Set column data through a given consumer, which eventually sets the value into the prepared statement. If the row index was specified previously, only the indexed row is set. Otherwise, all rows are set with the given function.
     */
    @Impure
    public <T> @Nonnull FreezableArrayList<SQLStatementProcessing<P>> setColumnData(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> function, @Nonnull T value) {
        final @Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>> preparedStatementEntries = pollPreparedStatementEntry();
        final @Nonnull FreezableArrayList<SQLStatementProcessing<P>> result = FreezableArrayList.withNoElements();
        for (@Nonnull SQLStatementEntry<P> sqlStatementEntry : preparedStatementEntries) {
            if (rowIndex == -1) {
                result.add(sqlStatementEntry.setColumnDataAllRows(function, value));
            } else {
                result.add(sqlStatementEntry.setColumnDataInRow(function, value, rowIndex));
            }
        }
        return result;
    }
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the prepared statement entry queue by the order of execution in the converter.
     */
    @Pure
    private @Nonnull LinkedList<@Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>>> initializePreparedStatementEntryQueue(FreezableArrayList<@Nonnull ? extends SQLStatementProcessing<P>> rowsOfOrderedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution) {
        final LinkedList<@Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>>> result = FreezableLinkedList.withNoElements();
        for (@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>> statementIndices : orderOfExecution) {
            final @Nonnull FreezableArrayList<@Nonnull SQLStatementEntry<P>> preparedStatementEntries = FreezableArrayList.withInitialCapacity(statementIndices.size());
            for (@Nonnull Pair<@Nonnull Integer, @Nonnull Integer> statementIndex : statementIndices) {
                preparedStatementEntries.add(new SQLStatementEntry<>(rowsOfOrderedStatements.get(statementIndex.get0()), statementIndex.get1()));
            }
            result.add(preparedStatementEntries);
        }
        return result;
    }
    
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new execution data object with the given rows of ordered statements and the given column count for group list.
     */
    private ExecutionData(@Nonnull FreezableArrayList<@Nonnull ? extends SQLStatementProcessing<P>> rowsOfOrderedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, @Nonnull LinkedList<@Nonnull Integer> columnCountForGroup) {
        this.preparedStatementEntryQueue = initializePreparedStatementEntryQueue(rowsOfOrderedStatements, orderOfExecution);
        this.rowsOfOrderedStatements = rowsOfOrderedStatements;
        this.columnCountForGroup = columnCountForGroup;
    }
    
    /**
     * Returns an execution data object with the given rows of ordered statements and the given column count for group list.
     */
    @Pure
    public static <P> @Nonnull ExecutionData<P> with(@Nonnull FreezableArrayList<@Nonnull ? extends SQLStatementProcessing<P>> rowsOfOrderedStatements, @Nonnull FreezableArrayList<@Nonnull FreezableArrayList<@Nonnull Pair<@Nonnull Integer, @Nonnull Integer>>> orderOfExecution, @Nonnull LinkedList<@Nonnull Integer> columnCountForGroup) {
        return new ExecutionData<>(rowsOfOrderedStatements, orderOfExecution, columnCountForGroup);
    }
    
}
