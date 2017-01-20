package net.digitalid.database.jdbc.encoder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.encoder.SQLDataManipulationLanguageEncoder;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public class JDBCDataManipulationLanguageEncoder extends JDBCEncoder implements SQLDataManipulationLanguageEncoder {
    
    protected JDBCDataManipulationLanguageEncoder(@Nonnull PreparedStatement preparedStatement) {
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
