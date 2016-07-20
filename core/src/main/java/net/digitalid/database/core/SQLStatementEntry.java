package net.digitalid.database.core;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.Positive;

import net.digitalid.database.core.processing.ParameterFunctionData;
import net.digitalid.database.core.processing.SQLStatementProcessing;

/**
 * The data structure that points to a certain column of an SQL statement.
 */
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
