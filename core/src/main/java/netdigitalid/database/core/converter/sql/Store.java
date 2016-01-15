package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.freezable.NonFrozen;
import net.digitalid.utility.validation.reference.Capturable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Stateless;
import net.digitalid.utility.validation.state.Validated;

import net.digitalid.database.core.Database;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;

/**
 * This is a utility class to store objects that implement {@link SQL} in the {@link Database}.
 */
@Stateless
public final class Store {
    
    /**
     * Returns the value of the given object for each column.
     * 
     * @param object the object whose values are to be returned.
     * 
     * @return the value of the given object for each column.
     * 
     * @ensure return.size() == object.getSQLConverter().getDeclaration().getNumberOfColumns() : "The returned array contains a value for each column.";
     */
    @Pure
    public static @Capturable @Nonnull @NonNullableElements @NonFrozen <O extends SQL<O, ?>> FreezableArray<String> getValues(@Nonnull O object) {
        return object.getSQLConverter().getValues(object);
    }
    
    /**
     * Returns the values of the given object separated by commas.
     * 
     * @param object the object whose values are to be returned.
     * 
     * @return the values of the given object separated by commas.
     */
    @Pure
    public static @Nonnull <O extends SQL<O, ?>> String getInsertForStatement(@Nonnull O object) {
        return object.getSQLConverter().getInsertForStatement(object);
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     */
    @Pure
    public static @Nonnull <O extends SQL<O, ?>> String getUpdateForStatement(@Nonnull O object) {
        return object.getSQLConverter().getUpdateForStatement(object);
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public static @Nonnull <O extends SQL<O, ?>> String getConditionForStatement(@Nonnull O object, @Nullable @Validated String alias) {
        return object.getSQLConverter().getConditionForStatement(object, alias);
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public static @Nonnull <O extends SQL<O, ?>> String getConditionForStatement(@Nonnull O object) {
        return object.getSQLConverter().getConditionForStatement(object);
    }
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given non-nullable object.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param preparedStatement the prepared statement whose parameters are to be set.
     * @param parameterIndex the starting index of the parameters which are to be set.
     */
    public static <O extends SQL<O, ?>> void nonNullable(@Nonnull O object, @NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        object.getSQLConverter().storeNonNullable(object, preparedStatement, parameterIndex);
    }
    
}
