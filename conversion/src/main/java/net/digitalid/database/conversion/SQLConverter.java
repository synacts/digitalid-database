package net.digitalid.database.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.interfaces.SelectionResult;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnConstraint;
import net.digitalid.database.dialect.ast.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.ast.statement.table.create.SQLType;
import net.digitalid.database.exceptions.operation.FailedValueRestoringException;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.exceptions.state.value.CorruptNullValueException;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableHashSet;
import net.digitalid.utility.collections.freezable.FreezableSet;
import net.digitalid.utility.collections.readonly.ReadOnlySet;
import net.digitalid.utility.conversion.Converter;
import net.digitalid.utility.conversion.exceptions.ConverterNotFoundException;
import net.digitalid.utility.conversion.exceptions.StoringException;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Stateless;

/**
 */
@Stateless
public abstract class SQLConverter<T> extends Converter {
    
    /* -------------------------------------------------- Converting -------------------------------------------------- */
    
    protected void convertNullable(@Nullable Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull SQLInsertStatement sqlInsertStatement) throws FailedValueStoringException {
/*       if (object == null) {
           // TODO: what about prefixes?!
           // TODO: move to SQLNullConverter
           sqlInsertStatement.setColumnName(type.getSimpleName());
           sqlInsertStatement.setColumnValue(null);
        } else {
            convertNonNullable(object, type, sqlInsertStatement);
        }*/
    }
    
    protected abstract void convertNonNullable(@Nonnull Object object, @Nonnull Class<?> type, @NonCapturable @Nonnull SQLInsertStatement sqlInsertStatement) throws FailedValueStoringException, ConverterNotFoundException, StoringException;
    
    /* -------------------------------------------------- Recovery -------------------------------------------------- */
    
    protected @Nonnull Object recoverNonNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException {
        @Nullable Object object = recoverNullable(type, result);
        if (object == null) {
            throw CorruptNullValueException.get();
        }
        return object;
    }
    
    protected abstract @Nullable Object recoverNullable(@Nonnull Class<?> type, @NonCapturable @Nonnull SelectionResult result) throws CorruptNullValueException, FailedValueRestoringException;
    
    private @Nonnull @Frozen ReadOnlySet<SQLColumnConstraint> getConstraintsFromAnnotations(@Nonnull Field field) {
        final @Nonnull @NonNullableElements Annotation[] annotations = field.getAnnotations();
        final @Nonnull @NonNullableElements FreezableSet<SQLColumnConstraint> columnConstraints = FreezableHashSet.get();
        for (@Nonnull Annotation annotation : annotations) {
            // TODO: discuss if it is ok to process the annotation in the AST. Alternatively, the constraints must be moved out of the dialect and into
            // the converter package. The columnConstraint should then only accept the SQL expression, if needed.
            columnConstraints.add(SQLColumnConstraint.of(annotation, field));
        }
        return columnConstraints.freeze();
    }
    
    public @Nonnull SQLColumnDeclaration createColumnDeclaration(@Nonnull String tableName, @Nonnull Field field) throws InternalException {
        @Nonnull SQLQualifiedColumnName qualifiedColumnName = SQLQualifiedColumnName.get(field.getName(), tableName);
        // TODO: move SQLType.of(...) to converter pkg
        @Nonnull SQLType sqlType = SQLType.of(field.getType());
        @Nonnull @Frozen ReadOnlySet<SQLColumnConstraint> columnConstraints = getConstraintsFromAnnotations(field);
        return SQLColumnDeclaration.with(qualifiedColumnName, sqlType, columnConstraints);
    }
    
}
