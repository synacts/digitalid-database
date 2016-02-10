package net.digitalid.database.conversion.value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public abstract class SQLSingleRowConverter<T> extends SQLConverter<T> {
    
    public abstract void collectValues(@Nullable Object object, Class<?> type, @NonCapturable @Nonnull SQLValues values) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException;
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> valuesList, @Nonnull Annotation[] annotations) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        if (valuesList.isEmpty()) {
            valuesList.add(SQLValues.get());
        }
        for (@Nonnull @NullableElements SQLValues values : valuesList) {
            collectValues(object, type, values);
        }
    }
    
    // TODO: implement
    @Override 
    public void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nullable @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(columnName, tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override 
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations, Site site, @Nullable @NonNullableElements Annotation[] annotations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: column-prefixes?
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(columnName), getSQLType(type, annotations), SQLColumnDefinition.of(annotations), SQLColumnConstraint.of(annotations, columnName));
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Field field, @Nonnull Site site) throws StructureException, StoringException, FailedNonCommittingOperationException {
    }
    
    @Override
    public final void createDependentTables(@Nonnull Table referencedTable, @Nonnull Field field, @Nonnull Site site) {
        
    }
    
    @Override
    public void createRequiredTables(@Nonnull Field field, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {
        
    }
    
    @Override 
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        return null;
    }
    
}
