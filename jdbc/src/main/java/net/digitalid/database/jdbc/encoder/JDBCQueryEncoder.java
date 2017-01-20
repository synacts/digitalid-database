package net.digitalid.database.jdbc.encoder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.exceptions.DatabaseExceptionBuilder;
import net.digitalid.database.interfaces.SQLDecoder;
import net.digitalid.database.interfaces.encoder.SQLDataManipulationLanguageEncoder;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
public class JDBCQueryEncoder extends JDBCEncoder implements SQLDataManipulationLanguageEncoder {
    
    protected JDBCQueryEncoder(@Nonnull PreparedStatement preparedStatement) {
        super(preparedStatement);
    }
    
    /* -------------------------------------------------- Execution -------------------------------------------------- */
       
    @Override
    @PureWithSideEffects
    public SQLDecoder execute() throws DatabaseException {
        try {
            final @Nonnull ResultSet resultSet = preparedStatement.executeQuery();
//            return JDBCSQLDecoderBuilder.withResultSet(resultSet).build();
            return null;
        } catch (SQLException exception) {
            throw DatabaseExceptionBuilder.withCause(exception).build();
        }
    }
    
}
