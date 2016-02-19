package net.digitalid.database.conversion.converter.primitive;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.conversion.reflection.exceptions.StructureException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.converter.SQLConverter;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.table.Table;
import net.digitalid.database.exceptions.operation.FailedNonCommittingOperationException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * This abstract class implements common methods for convertible types that can be stored in a single row.
 */
public abstract class SQLSingleRowConverter<T> extends SQLConverter<T> {
    
    /**
     * Collects a non-null value and stores it in the SQLValues object.
     */
    public abstract void collectNonNullValues(@Nonnull Object object, Class<?> type, @Nonnull @NonCapturable SQLValues values) throws StoringException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException;
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull @NonCapturable @NonNullableElements FreezableArrayList<SQLValues> valuesList) throws FailedValueStoringException, StoringException, StructureException, NoSuchFieldException {
        if (valuesList.isEmpty()) {
            valuesList.add(SQLValues.get());
        }
        for (@Nonnull @NullableElements SQLValues values : valuesList) {
            if (object == null) {
                values.addValue(null);
            } else {
                collectNonNullValues(object, type, values);
            }
        }
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override 
    public void putColumnNames(@Nonnull Class<?> type, @Nonnull String columnName, @Nullable String tableName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(columnName, tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    @Override 
    public void putColumnDeclarations(@Nonnull Class<?> type, @Nonnull String columnName, @Nonnull @NonNullableElements Annotation[] annotations, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
        // TODO: column-prefixes?
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(columnName), getSQLType(type, annotations), SQLColumnDefinition.of(annotations), SQLColumnConstraint.of(annotations, columnName));
        columnDeclarations.add(columnDeclaration);
    }
    
    /* -------------------------------------------------- Required and Dependent Tables -------------------------------------------------- */
    
    @Override
    public void createRequiredTables(@Nonnull Class<?> type, @Nonnull @NonNullableElements Annotation[] annotations, @Nonnull Site site) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {}
    
    @Override
    public void createDependentTables(@Nonnull Table referencedTable, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws NoSuchFieldException, StructureException, FailedNonCommittingOperationException {}
    
    @Override
    public void insertIntoDependentTable(@Nonnull Table referencedTable, @Nonnull @NonNullableElements FreezableArrayList<SQLValues> primaryKeyTableCells, @Nullable Object object, @Nonnull Class<?> type, @Nonnull String nameOfDependentTable, @Nonnull @NonNullableElements Annotation[] annotations) throws StructureException, StoringException, FailedNonCommittingOperationException {}
    
}
