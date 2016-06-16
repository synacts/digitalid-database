package net.digitalid.database.conversion.converter;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.conversion.exceptions.NoSQLTypeRepresentationException;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 * This class represents the abstract form of an SQL converter.
 * The implementations of the SQL converter are responsible for providing information about the SQL Table definition and content of the SQL table.
 * The main tasks are to collect column declarations and column names for constructing SQL statements, values for inserting data via an SQL insert
 * statement and recovering objects through selection results.
 * The converter also should provide information about which SQL type it converts, if possible.
 * Optionally, it is possible to create required or dependent tables, if the structure of the type requires it.
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    /**
     * Returns the SQL type to and from which the converter converts to. Throws a NoSQLTypeRepresentationException 
     * if the converter does not represent a single SQL type, but rather a list of complex types.
     */
    public abstract @Nonnull SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSQLTypeRepresentationException;
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    /**
     * This method collects the column names used to insert or query an SQL table. If the type is a primitive (or single-row type), the column name will 
     * be inserted into the list of columnNames. Otherwise, the fields of the type are iterated and the column names are stored.
     * If a tableName is provided, the qualified column name is stored.
     */
    // TODO: reformat such that type, tableName and annotations are part of field- or column-metadata.
    public abstract void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull @NonCapturable @NonNullableElements FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException;
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    /**
     * This method collects the column declarations required to construct the SQL create statement. 
     */
    public abstract void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException;
    
    /* -------------------------------------------------- Required and Dependent Tables -------------------------------------------------- */
    
    /**
     * Creates tables that are required to exist prior to the table that is constructed for the given type.
     */
    public abstract void createRequiredTables(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException;
    
    /**
     * Creates tables that depend on the table that was created for the given type.
     */
    public abstract void createDependentTables(@Nonnull Table referencedTable, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException;
    
    /**
     * If required, inserts values into the dependent table after the object of a certain type was converted.
     */
    public abstract void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws StructureException, StoringException, FailedNonCommittingOperationException;
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    /**
     * Collects the values from the given object by storing it in the list of SQLValues. Each list entry represents a row in the SQL table. In other words,
     * for each list entry, an SQL insert statement will be issued.
     * The type and annotations are used to determine how the object is going to be represented.
     */
    public abstract void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException;
    
   
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    /**
     * Recovers an object of a given type from a given selection result. If the recovered object is null, a corrupt-null-value exception is thrown.
     */
    public @Nonnull T recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        @Nullable T object = recoverNullable(type, result, annotations);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    /**
     * Recovers an object of a given type from a given selection result. The result may be null if the selection result is null.
     */
    public abstract @Nullable T recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result, @Nonnull @NonNullableElements Annotation[] annotations) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException;
    
    
}
