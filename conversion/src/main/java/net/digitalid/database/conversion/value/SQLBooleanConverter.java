package net.digitalid.database.conversion.value;

import java.lang.reflect.Field;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.reflection.exceptions.StructureException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLValues;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDefinition;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;

/**
 *
 */
public class SQLBooleanConverter extends SQLConverter<Boolean> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public SQLType getSQLType(Field field) {
        return SQLType.BOOLEAN;
    }
    
    /* -------------------------------------------------- Value Collection -------------------------------------------------- */
    
    @Override
    public void collectValues(@Nullable Object object, @Nonnull Class<?> type, @Nonnull @NullableElements SQLValues values) throws FailedValueStoringException {
        Require.that(object == null || object instanceof Boolean).orThrow("The object is of type boolean.");
        
        final @Nonnull SQLBooleanLiteral booleanLiteral = SQLBooleanLiteral.get((Boolean) object);
        values.addValue(booleanLiteral);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) {
        Require.that(Boolean.class.isAssignableFrom(field.getType())).orThrow("The field has the type 'Boolean'");
        // TODO: column-prefixes?
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLColumnName.get(field.getName()), SQLType.BOOLEAN, SQLColumnDefinition.of(field), SQLColumnConstraint.of(field));
        columnDeclarations.add(columnDeclaration);
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @Override
    @Nullable
    public Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        final @Nullable Boolean value = result.getBoolean();
        return value;
    }
    
}
