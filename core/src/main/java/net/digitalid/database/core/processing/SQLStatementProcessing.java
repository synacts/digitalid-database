package net.digitalid.database.core.processing;

import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.type.Mutable;

/**
 * The SQL statement processing sets parameter functions into columns of rows, multiplies rows and adds the rows to a prepared statement batch.
 */
@Mutable
public interface SQLStatementProcessing<P> {
    
    /* -------------------------------------------------- Sets Column Parameter Function -------------------------------------------------- */
    
    /**
     * Sets the function, that will be executed when the prepared statement is created, into the column of all rows.
     */
    @Impure
    public <T> void setColumnParameterFunctionInAllRows(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> parameterFunction, @Nonnull T parameterValue, @Nonnull Integer parameterIndex);
    
    /**
     * Sets the function, that will be executed when the prepared statement is created, into the column of a specific row.
     */
    @Impure
    public <T> void setColumnParameterFunctionInRow(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<P, T>, SQLException> parameterFunction, @Nonnull T parameterValue, @Nonnull Integer parameterIndex, int row);
    
    /* -------------------------------------------------- Row Multiplication -------------------------------------------------- */
    
    /**
     * Multiplies all rows by the given number.
     */
    @Impure
    public void multiplyRows(@Positive int number);
    
    /**
     * Multiplies a specific row by the given number.
     */
    @Impure
    void multiplyRow(@Positive int row, @NonNegative int number);
    
    /* -------------------------------------------------- Prepared Statements -------------------------------------------------- */
    
    /**
     * Adds the rows to the prepared statement batch. Requires that all columns are set at the point of calling this method.
     */
    @Impure
    public void addRowsToPreparedStatementBatch() throws SQLException;
    
    /**
     * Returns the prepared statement.
     */
    @Pure
    public P getPreparedStatement();
    
}
