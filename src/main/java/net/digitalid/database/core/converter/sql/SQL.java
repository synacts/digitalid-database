package net.digitalid.database.core.converter.sql;

import java.sql.ResultSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import jdk.nashorn.internal.ir.Block;
import net.digitalid.database.core.Database;
import net.digitalid.database.core.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.core.exceptions.state.value.CorruptNullValueException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.sql.identifier.SQLName;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.core.table.Table;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.ConverterAnnotations;
import net.digitalid.utility.conversion.Convertible;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.exceptions.StructureException;
import net.digitalid.utility.exceptions.external.InvalidEncodingException;
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
    public static @Nullable Convertible recoverNullable(@Nonnull Class<? extends Convertible> type, @Nonnull SQLWhereClause whereClause, @Nonnull Site schema) throws InternalException, RecoveryException, CorruptNullValueException, FailedValueRestoringException {
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
     * Recovers a non-nullable, convertible object from a non-nullable XDF block.
     */
    public static @Nonnull Convertible recoverNonNullable(@Nonnull Block block, @Nonnull Class<? extends Convertible> type) throws InvalidEncodingException, RecoveryException, InternalException  {

    }
    
    /* -------------------------------------------------- Convert To -------------------------------------------------- */

    /**
     * Converts a nullable object into a nullable XDF block.
     */
    public static @Nullable Block convertNullable(@Nullable Convertible convertible, @Nonnull Class<? extends Convertible> type) throws StoringException, InternalException {
       return convertible == null ? null : convertNonNullable(convertible, type); 
    }

    /**
     * Converts a nullable object into a nullable XDF block using an optional parent name.
     */
    public static @Nullable Block convertNullable(@Nullable Convertible convertible, @Nonnull Class<? extends Convertible> type, @Nullable String parentName) throws StoringException, InternalException {
        return convertible == null ? null : convertNonNullable(convertible, type, parentName);
    }

    /**
     * Converts a non-nullable object into a non-nullable XDF block.
     *
     * @param convertible the convertible object which is converted into an XDF block.
     * @param type the type of the object which should be converted.
     * 
     * @return a non-nullable XDF block converted from a non-nullable value.
     *
     * @throws StoringException if no converter for this type could be found.
     */
    public static @Nonnull Block convertNonNullable(@Nonnull Convertible convertible, @Nonnull Class<? extends Convertible> type) throws StoringException, InternalException {
        return convertNonNullable(convertible, type, null);
    }

    /**
     * Converts a non-nullable object into a non-nullable XDF block with an optionally given parent name.
     *
     * @param convertible the convertible object which is converted into an XDF block.
     * @param type the type of the object which should be converted.
     * @param parentName the parent name of the object which should be converted.
     *
     * @return a non-nullable XDF block converted from a non-nullable value.
     *
     * @throws StoringException if no converter for this type could be found.
     */
    @SuppressWarnings("unchecked")
    public static @Nonnull Block convertNonNullable(@Nonnull Convertible convertible, @Nonnull Class<? extends Convertible> type, @Nullable String parentName) throws StoringException, InternalException {

        @Nonnull Block serializedObject;
        final @Nonnull Converter.Structure structure;
        try {
            structure = Converter.inferStructure(type);
        } catch (StructureException e) {
            throw StoringException.get(type, e.getMessage(), e);
        }
        final @Nonnull ConverterAnnotations annotations = ConverterAnnotations.get();
        switch (structure) {
            case TUPLE:
                serializedObject = XDFFormat.TUPLE_CONVERTER.convertNonNullable(convertible, type, type.getSimpleName(), parentName, annotations);
                break;
            case SINGLE_TYPE:
                serializedObject = XDFFormat.SINGLE_FIELD_CONVERTER.convertNonNullable(convertible, type, type.getSimpleName(), parentName, annotations);
                break;
            default:
                throw InternalException.get("Structure '" + structure + "' is unknown. Known types are: '" + Arrays.toString(Converter.Structure.values()) + "'.");
        }
        return serializedObject;
    }
    
}
