package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.annotations.NonCommitting;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.exceptions.state.value.CorruptValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.identifier.SQLPrefix;
import net.digitalid.utility.annotations.reference.Capturable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Matching;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.converter.IterableConverter;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.system.exceptions.internal.InternalException;

/**
 * An SQL converter allows to store and restore objects into and from the {@link Database database}.
 * 
 * @param <O> the type of the objects that this converter can store and restore, which is typically the surrounding class.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an {@link Object}.
 * 
 * @see SQL
 * @see ChainingSQLConverter
 */
@Immutable
public abstract class SQLConverter<O, E> {
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    /**
     * Stores the declaration of this SQL converter.
     */
    private final @Nonnull Declaration declaration;
    
    /**
     * Returns the declaration of this SQL converter.
     * 
     * @return the declaration of this SQL converter.
     */
    @Pure
    public final @Nonnull Declaration getDeclaration() {
        return declaration;
    }
    
    /**
     * Returns this SQL converter redeclared with the given declaration.
     * 
     * @param declaration the declaration that replaces the declaration.
     * 
     * @return this SQL converter redeclared with the given declaration.
     */
    @Pure
    public final @Nonnull SQLConverter<O, E> redeclaredWith(@Nonnull @Matching Declaration declaration) {
        return RedeclaringSQLConverter.get(declaration, this);
    }
    
    /**
     * Returns this SQL converter redeclared with a prefixed declaration.
     * 
     * @param prefix the prefix used to prefix the existing declaration.
     * 
     * @return this SQL converter redeclared with a prefixed declaration.
     */
    @Pure
    public final @Nonnull SQLConverter<O, E> prefixedWith(@Nonnull SQLPrefix prefix) {
        return RedeclaringSQLConverter.get(declaration.prefixedWith(prefix), this);
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL converter with the given declaration.
     * 
     * @param declaration the declaration of the new SQL converter.
     */
    protected SQLConverter(@Nonnull Declaration declaration) {
        this.declaration = declaration;
    }
    
    /* -------------------------------------------------- Storing (with Statement) -------------------------------------------------- */
    
    /**
     * Returns the value of the given object or 'NULL' for each column.
     * 
     * @param object the nullable object whose values are to be returned.
     * 
     * @return the value of the given object or 'NULL' for each column.
     * 
     * @ensure return.size() == getDeclaration().getNumberOfColumns() : "The returned array contains a value for each column.";
     */
    @Pure
    public final @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getValues(@Nullable O object) {
        final @Nonnull FreezableArray<String> values = FreezableArray.get(declaration.getNumberOfColumns());
        // TODO: The necessary methods no longer exist.
        return values;
    }
    
    // Most of this section will hopefully soon be replaced by the SQL builder.
    
    /**
     * Returns the values of the given object separated by commas.
     * 
     * @param object the object whose values are to be returned.
     * 
     * @return the values of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getInsertForStatement(@Nullable O object) {
        return IterableConverter.toString(getValues(object));
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @param object the object whose values are to be used for equality.
     * @param alias the table alias that is to be prepended to all column names.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object.
     * 
     * @ensure return.size() == getDeclaration().getNumberOfColumns() : "The returned array contains an entry for each column.";
     */
    @Pure
    private @Capturable @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> getColumnsEqualValues(@Nullable O object, @Nullable @Validated String alias) {
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> names = declaration.getColumnNames(alias);
        final @Nonnull @NonNullableElements @NonFrozen FreezableArray<String> values = getValues(object);
        for (int i = 0; i < names.size(); i++) {
            names.set(i, names.getNonNullable(i) + " = " + values.getNonNullable(i)); 
        }
        return names;
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by commas.
     */
    @Pure
    public final @Nonnull String getUpdateForStatement(@Nullable O object) {
        return IterableConverter.toString(getColumnsEqualValues(object, null));
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
    public final @Nonnull String getConditionForStatement(@Nullable O object, @Nullable @Validated String alias) {
        return IterableConverter.toString(getColumnsEqualValues(object, alias), " AND ");
    }
    
    /**
     * Returns the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     * 
     * @param object the object whose values are to be used for equality.
     * 
     * @return the name of each column followed by the equality sign and the corresponding value of the given object separated by {@code AND}.
     */
    @Pure
    public final @Nonnull String getConditionForStatement(@Nullable O object) {
        return getConditionForStatement(object, null);
    }
    
    /* -------------------------------------------------- Storing -------------------------------------------------- */
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given non-nullable object.
     * The number of parameters that are set is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param object the non-nullable object which is to be stored in the database.
     * @param collector the value collector used to store the values of the object.
     */
    @NonCommitting
    public abstract void storeNonNullable(@Nonnull O object, @NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException;
    
    /**
     * Sets the parameters starting from the given index of the prepared statement to the given nullable object.
     * The number of parameters that are set is given by {@link Declaration#getNumberOfColumns()}.
     * 
     * @param object the nullable object which is to be stored in the database.
     * @param collector the value collector used to store the values of the object.
     */
    @NonCommitting
    public final void storeNullable(@Nullable O object, @NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        if (object == null) { declaration.storeNull(collector); }
        else { SQLConverter.this.storeNonNullable(object, collector); }
    }
    
    /* -------------------------------------------------- Restoring -------------------------------------------------- */
    
    /**
     * Returns a nullable object from the external object and selection result.
     * 
     * @param external the external object which is needed to recover the object.
     * @param result the selection result used to restore the values of the object.
     * 
     * @return a nullable object from the external object and selection result.
     */
    @NonCommitting
    public abstract @Nullable O restoreNullable(@Nonnull E external, @NonCapturable @Nonnull SelectionResult result) throws FailedValueRestoringException, CorruptValueException, InternalException;
    
    /**
     * Returns a non-nullable object from the external object and selection result.
     * 
     * @param external the external object which is needed to recover the object.
     * @param result the selection result used to restore the values of the object.
     * 
     * @return a non-nullable object from the external object and selection result.
     */
    @NonCommitting
    public final @Nonnull O restoreNonNullable(@Nonnull E external, @NonCapturable @Nonnull SelectionResult result) throws FailedValueRestoringException, CorruptValueException, InternalException {
        final @Nullable O object = restoreNullable(external, result);
        if (object == null) { throw CorruptNullValueException.get(); }
        return object;
    }
    
}
