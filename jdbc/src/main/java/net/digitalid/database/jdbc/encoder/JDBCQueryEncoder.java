package net.digitalid.database.jdbc.encoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;
import net.digitalid.database.jdbc.decoder.JDBCDecoderBuilder;

/**
 * The JDBC query encoder collects values for the prepared statement and executes it. Upon successful execution 
 * it returns an SQL decoder, which can be used to recover the data.
 */
@GenerateBuilder
@GenerateSubclass
public class JDBCQueryEncoder extends JDBCEncoderSubclass implements SQLQueryEncoder {
    
    protected JDBCQueryEncoder(@Nonnull PreparedStatement preparedStatement) {
        super(preparedStatement);
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
       
    @Override
    @PureWithSideEffects
    public @Nonnull SQLDecoder execute() throws DatabaseException {
        try {
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
            return JDBCDecoderBuilder.withResultSet(resultSet).build();
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
