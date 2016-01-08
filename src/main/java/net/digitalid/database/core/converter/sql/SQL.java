package net.digitalid.database.core.converter.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.interfaces.jdbc.JDBCValueCollector;
import net.digitalid.database.core.sql.identifier.SQLName;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.ConverterAnnotations;
import net.digitalid.utility.conversion.Convertible;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 */
@Stateless
public final class SQL {
    
     /* -------------------------------------------------- Format -------------------------------------------------- */
    
    /**
     * The format object of SQL, which contains the SQL converters.
     */
    public static final @Nonnull SQLFormat FORMAT = new SQLFormat();
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */

    /**
     * Recovers a nullable, convertible object from a SQL table.
     */
    public static @Nullable Convertible recoverNullable(@Nonnull SQLWhereClause whereClause, @Nonnull Class<? extends Convertible> type, @Nonnull Site schema) throws InternalException, RecoveryException, CorruptNullValueException, FailedValueRestoringException {
        ConverterAnnotations converterAnnotations = Converter.getAnnotations(type);

        Table table = Table.get(SQLName.get(type.getSimpleName()));
        SelectionResult result = table.select(schema).where(whereClause);
        Object object = SQLFormat.CONVERTIBLE_CONVERTER.recoverNullable(type, result);
        if (!type.isInstance(object)) {
            throw RecoveryException.get(type, "The converter failed to recover the object of type '" + type + "' from the SQL table");
        }
        return type.cast(object);
    }

    /**
     * Recovers a non-nullable, convertible object from a SQL table.
     */
    public static @Nonnull Convertible recoverNonNullable(@Nonnull SQLWhereClause whereClause, @Nonnull Class<? extends Convertible> type, @Nonnull Site schema) throws InternalException, RecoveryException, CorruptNullValueException, FailedValueRestoringException {
        final @Nullable Convertible convertible = recoverNullable(whereClause, type, schema);
        if (convertible == null) {
            throw CorruptNullValueException.get();
        }
        return convertible;
    }
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */

    /**
     * Stores a nullable object into a SQL table.
     */
    // TODO: do we even care about nullable / non-nullable??
    public static void insert(@Nullable Convertible convertible, @Nonnull Class<? extends Convertible> type) throws StoringException, InternalException {

        SQLInsertStatement insertStatement = new SQLInsertStatement();
        
    }
    
}
