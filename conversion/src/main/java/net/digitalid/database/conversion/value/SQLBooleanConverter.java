package net.digitalid.database.conversion.value;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.annotations.elements.NullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.conversion.ConverterAnnotations;
import net.digitalid.utility.validation.reference.NonCapturable;

import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLBooleanConverter extends SQLConverter<Boolean> {
    
    @Override
    protected void putColumnNames(@Nonnull Field field, @Nullable SQLQualifiedTableName qualifiedTableName, @Nonnull FreezableArrayList<SQLQualifiedColumnName> qualifiedColumnNames) {
        SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), qualifiedTableName.tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    @Override
    protected void collectNonNullValues(@Nonnull Object object, @Nonnull @NullableElements SQLValues values) throws FailedValueStoringException {
        assert object instanceof Boolean : "The object is of type boolean.";
        
        final @Nonnull SQLBooleanLiteral booleanLiteral = SQLBooleanLiteral.get((Boolean) object);
        values.addValue(booleanLiteral);
    }
    
    @Override
    protected void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) {
        assert Boolean.class.isAssignableFrom(field.getType()) : "The field has the type 'Boolean'";
        // TODO: column-prefixes?
        final @Nonnull String columnName = field.getName();
        final @Nonnull ConverterAnnotations converterAnnotations = getAnnotations(field);
        // TODO: get all the Column Constraint annotations and set the columnConstraint in the columnDeclaration
//        final Default defaultValue = (Default) converterAnnotations.get(Default.class);
//        final Nonnull nonnull = (Nonnull) converterAnnotations.get(Nonnull.class);
        
        final @Nonnull SQLColumnConstraint columnConstraint = SQLColumnConstraint.get();
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.get(columnName, columnConstraint);
        columnDeclarations.add(columnDeclaration);
    }
    
    @Override
    protected @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        final @Nullable Boolean value = result.getBoolean();
        return value;
    }
    
}
