package net.digitalid.database.conversion.value.integer;

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
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.conversion.SQLConverter;
import net.digitalid.database.conversion.value.SQLSingleRowConverter;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberLiteral;
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
public class SQLInteger32Converter extends SQLSingleRowConverter<Integer> {
    
    /* -------------------------------------------------- SQL Type -------------------------------------------------- */
    
    @Override
    public SQLType getSQLType(Field field) {
        return SQLType.INTEGER32;
    }
    
    /* -------------------------------------------------- Value Collection -------------------------------------------------- */
    
    @Override
    public void collectValues(@Nullable Object object, Class<?> type, @NonCapturable @Nonnull SQLValues values) throws StoringException, ConverterNotFoundException, FailedValueStoringException, InternalException, StructureException, NoSuchFieldException {
        assert object == null || object instanceof Integer: "The object is of type integer32.";
        
        // TODO: handle null-cases properly
        final @Nonnull SQLNumberLiteral numberLiteral = SQLNumberLiteral.get((Integer) object);
        values.addValue(numberLiteral);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    public void putColumnNames(@Nonnull Field field, @Nullable String tableName, @NonCapturable @Nonnull FreezableList<? super SQLQualifiedColumnName> qualifiedColumnNames) throws StructureException, ConverterNotFoundException {
        final @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), tableName);
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    @Override
    public void putColumnDeclarations(@Nonnull Field field, @NonCapturable @Nonnull FreezableArrayList<SQLColumnDeclaration> columnDeclarations) throws ConverterNotFoundException, StructureException, NoSuchFieldException {
// TODO: Figure out what to do with the assert. If called from the CollectionsConverter we have the wrong type, but should we even care?
//        assert int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType()) : "The field has the type 'int' or 'Integer'";
        // TODO: column-prefixes?
        final @Nonnull SQLColumnDeclaration columnDeclaration = SQLColumnDeclaration.of(SQLQualifiedColumnName.get(field.getName()), SQLType.INTEGER32, SQLColumnDefinition.of(field), SQLColumnConstraint.of(field));
        columnDeclarations.add(columnDeclaration);
    }
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    @Override
    public @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException, StructureException, ConverterNotFoundException, RecoveryException {
        final @Nullable Integer value = result.getInteger32();
        return value;
    }
}
