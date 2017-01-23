package net.digitalid.database.jdbc.encoder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.encoder.SQLActionEncoder;

/**
 * The JDBC action encoder collects values for the prepared statement and executes it.
 */
@GenerateBuilder
@GenerateSubclass
public class JDBCActionEncoder extends JDBCEncoderSubclass implements SQLActionEncoder {
    
    protected JDBCActionEncoder(@Nonnull PreparedStatement preparedStatement) {
        super(preparedStatement);
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
       
    @Override
    @PureWithSideEffects
    public void execute() throws DatabaseException {
        try {
            preparedStatement.execute();
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
