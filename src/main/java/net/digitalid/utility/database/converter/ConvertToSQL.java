package net.digitalid.utility.database.converter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.database.configuration.Database;

/**
 * This is a utility class to store objects that implement {@link SQL} in the {@link Database}.
 */
@Stateless
public final class ConvertToSQL {
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given non-nullable object.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    public static <V extends SQL<V, ?>> void nonNullable(@Nonnull V object, @Nonnull PreparedStatement preparedStatement, int parameterIndex) throws SQLException {
        object.getSQLConverter().storeNonNullable(object, preparedStatement, parameterIndex);
    }
    
}
