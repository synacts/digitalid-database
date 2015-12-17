package net.digitalid.database.core.converter.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.annotation.Nonnull;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.system.exceptions.internal.InternalException;

/**
 * A multiple-row SQL converter allows to store and restore set- or map-based objects into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 */
@Immutable
public abstract class MultipleRowSQLConverter<O, E> extends SQLConverter<O, E> {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL converter with the given declaration.
     * 
     * @param declaration the declaration of the new SQL converter.
     */
    protected MultipleRowSQLConverter(@Nonnull Declaration declaration) {
        super(declaration);
    }
    
    /* -------------------------------------------------- Multiple-Row Storing -------------------------------------------------- */
    
    /**
     * Sets the parameters starting from the given index of the prepared statement multiple times to the given non-nullable object.
     * This method can {@link PreparedStatement#addBatch() add to the batch} multiple times, thus all other parameters must already be set.
     * The number of parameters that are set is given by {@link Declaration#getNumberOfColumns() getDeclaration().getNumberOfColumns()}.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    @NonCommitting
    public abstract void storeMultipleRows(@Nonnull O object, @NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException;
    
    /* -------------------------------------------------- Multiple-Row Restoring -------------------------------------------------- */
    
    /**
     * Returns a non-nullable object from the given columns of the result set.
     * This method iterates through the {@link ResultSet result set}, thus only the columns of the returned object should be selected.
     * The number of columns that are read is given by {@link Declaration#getNumberOfColumns() getDeclaration().getNumberOfColumns()}.
     * 
     * @param external the external object which is needed to recover the object.
     * @param resultSet the result set from which the data is to be retrieved.
     * @param columnIndex the starting index of the columns containing the data.
     * 
     * @return a non-nullable object from the given columns of the result set.
     */
    @Pure
    @NonCommitting
    public abstract @Nonnull O restoreMultipleRows(@Nonnull E external, @NonCapturable @Nonnull SelectionResult result) throws FailedValueRestoringException, CorruptValueException, InternalException;
    
}
