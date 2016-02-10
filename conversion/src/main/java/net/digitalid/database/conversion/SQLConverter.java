package net.digitalid.database.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Stateless;

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
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    public abstract @Nullable SQLType getSQLType(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations);
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    public abstract void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nonnull @NonNullableElements Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException;
    
    // TODO: reformat such that type, tableName and annotations are part of field- or column-metadata.
    public abstract void putColumnNames(@Nonnull Class<?> type, @Nullable String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException;
    
    public abstract void putColumnDeclarations(@Nonnull Class<?> type, @Nullable String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, Site site, @Nullable @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException;
    
    // TODO: ideally we should use the primary key of the referencedRow, instead of constructing one ourselves.
    public abstract void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException;
    
    /**
     * Creates tables for referenced columns.
     */
    public abstract void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException;
    
    public abstract void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException;
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    protected @Nonnull Object recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, RecoveryException, StructureException, ConverterNotFoundException {
        @Nullable Object object = recoverNullable(type, result);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    public abstract @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException;
    
}
