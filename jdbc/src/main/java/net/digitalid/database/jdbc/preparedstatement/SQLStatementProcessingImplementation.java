package net.digitalid.database.jdbc.preparedstatement;

import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.functional.failable.FailableConsumer;
import net.digitalid.utility.validation.annotations.size.MinSize;

import net.digitalid.database.interfaces.processing.ParameterFunctionData;
import net.digitalid.database.interfaces.processing.SQLRow;
import net.digitalid.database.interfaces.processing.SQLStatementProcessing;

/**
 * Implements the SQL statement processing for the JDBC provider via JDBC prepared statements.
 */
public class SQLStatementProcessingImplementation implements SQLStatementProcessing<@Nonnull PreparedStatement> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The prepared statement to be processed.
     */
    private final @Nonnull PreparedStatement preparedStatement;
    
    /**
     * The rows of the prepared statement.
     */
    private final @Nonnull @MinSize(1) FreezableArrayList<@Nonnull SQLRow<PreparedStatement>> rowsForStatement;
    
    /**
     * Returns an SQL statement processing implementation.
     */
    @Pure
    public static @Nonnull SQLStatementProcessingImplementation with(@Nonnull PreparedStatement preparedStatement) {
        return new SQLStatementProcessingImplementation(preparedStatement);
    }
    
    /* -------------------------------------------------- Sets Column Parameter Function -------------------------------------------------- */
    
    @Impure
    @Override
    public <T> void setColumnParameterFunctionInAllRows(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, T>, SQLException> parameterFunction, @Nonnull T parameterValue, @Nonnull Integer columnIndex) {
        for (@Nonnull SQLRow<PreparedStatement> row : rowsForStatement) {
            row.setColumnData(parameterFunction, parameterValue, columnIndex);
        }
    }
    
    @Impure
    @Override
    public <T> void setColumnParameterFunctionInRow(@Nonnull FailableConsumer<@Nonnull ParameterFunctionData<@Nonnull PreparedStatement, T>, SQLException> parameterFunction, @Nonnull T parameterValue, @Nonnull Integer parameterIndex, int row) {
        rowsForStatement.get(row).setColumnData(parameterFunction, parameterValue, parameterIndex);
    }
    
    /* -------------------------------------------------- Row Multiplication -------------------------------------------------- */
    
    @Impure
    @Override
    public void multiplyRows(int number) {
        final @Nonnull @MinSize(1) FreezableArrayList<@Nonnull SQLRow<PreparedStatement>> copyOfRowsForStatement = this.rowsForStatement.clone();
        for (int i = 1; i < number; i++) {
            for (@Nonnull SQLRow<PreparedStatement> sqlRow : copyOfRowsForStatement) {
                this.rowsForStatement.add(SQLRow.copy(sqlRow));
            }
        }
    }
    
    @Impure
    @Override
    public void multiplyRow(int rowIndex, int number) {
        final @Nonnull SQLRow<PreparedStatement> sqlRow = this.rowsForStatement.get(rowIndex);
        for (int i = 1; i < number; i++) {
            this.rowsForStatement.add(rowIndex, SQLRow.copy(sqlRow));
        }
    }
    
    /* -------------------------------------------------- Prepared Statements -------------------------------------------------- */
    
    @Impure
    @Override
    public void addRowsToPreparedStatementBatch() throws SQLException {
        for (@Nonnull SQLRow<PreparedStatement> row : rowsForStatement) {
            row.addColumnsToPreparedStatement(preparedStatement);
            preparedStatement.addBatch();
        }
    }
    
    @Pure
    @Override
    public @Nonnull PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL statement processing implementation object with a given prepared statement.
     */
    private SQLStatementProcessingImplementation(@Nonnull PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
        int columnCount = -1;
        try {
            final ResultSetMetaData metaData = preparedStatement.getMetaData();
            if (metaData != null) {
                columnCount = metaData.getColumnCount();
            }
        } catch (SQLException e) {
            // intentionally left blank. Metadata fetching not supported by driver.
        }
        final @Nonnull SQLRow<PreparedStatement> sqlRow;
        if (columnCount != -1) {
            sqlRow = SQLRow.with(columnCount);
        } else {
            sqlRow = SQLRow.withUnknownColumnCount();
        }
        this.rowsForStatement = FreezableArrayList.withElement(sqlRow);
    }
   
}
