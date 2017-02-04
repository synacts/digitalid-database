package net.digitalid.database.conversion.utility;

import java.sql.Types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.math.Negative;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.NonPositive;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.constraints.Unique;
import net.digitalid.database.annotations.type.Embedded;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteralBuilder;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLReference;
import net.digitalid.database.dialect.statement.table.create.SQLReferenceBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraint;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLPrimaryKeyConstraint;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLPrimaryKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.enumerations.ForeignKeyAction;
import net.digitalid.database.interfaces.encoder.SQLEncoder;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.unit.Unit;

/**
 *
 */
@Utility
public abstract class SQLConversionUtility {
    
    @Pure
    public static boolean isNotNull(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(Nonnull.class);
    }
    
    @Pure
    public static boolean isPrimaryKey(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(PrimaryKey.class);
    }
    
    @Pure
    public static boolean isUnique(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(Unique.class);
    }
    
    @Pure
    public static @Nonnull SQLReference isForeignKey(@Nonnull CustomField customField) {
//        SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schema).build();
//        SQLReferenceBuilder.withTable(table)
//        customField.isAnnotatedWith(Referenced.class);
        return null;
    }
    
    @Pure
    public static @Nullable CustomType getEmbeddedPrimitiveType(@Nonnull CustomType customType) {
        if (customType.isObjectType()) {
            return ((CustomType.CustomConverterType) customType).getConverter().getFields(Representation.INTERNAL).getFirst().getCustomType();
        }
        return null;
    }
    
    @Pure
    public static @Nullable SQLExpression getDefaultValue(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Default.class)) { return null; }
        final @Nonnull CustomAnnotation annotation = customField.getAnnotation(Default.class);
        final @Nullable String defaultValue = annotation.get("value", String.class);
        @Nullable CustomType customType = customField.getCustomType();
        if (customType.isObjectType()) {
            customType = getEmbeddedPrimitiveType(customType);
        }
        if (customType != null) {
            final int sqlType = SQLEncoder.getSQLType(customType);
            switch (sqlType) {
                case Types.BOOLEAN:
                    if (defaultValue == null) {
                        return SQLBooleanLiteral.NULL;
                    } else if (Boolean.parseBoolean(defaultValue)) {
                        return SQLBooleanLiteral.TRUE;
                    } else {
                        return SQLBooleanLiteral.FALSE;
                    }
                default:
                    return SQLStringLiteralBuilder.buildWithString(defaultValue);
            }
        } else {
            Log.warning("Ignoring default value for field $ of type $ in SQL conversion.", customField, customType);
            return null;
        }
    }
    
    @Pure
    @SuppressWarnings("all")
    private static @Nullable SQLBooleanExpression getMultipleOfCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(MultipleOf.class)) { return null; }
        final long multipleOfValue = customField.getAnnotation(MultipleOf.class).get("value", long.class);
        final @Nonnull SQLNumberComparisonBooleanExpression multipleOfValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.EQUAL)
                .withLeftExpression(SQLBinaryNumberExpressionBuilder.withOperator(SQLBinaryNumberOperator.MODULO)
                        .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                        .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(multipleOfValue)).build())
                .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(0L)).build();
        return multipleOfValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNonNegativeCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(NonNegative.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression nonNegativeValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER_OR_EQUAL)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(0L)).build();
        return nonNegativeValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNonPositiveCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(NonPositive.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression nonPositiveValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS_OR_EQUAL)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(0L)).build();
        return nonPositiveValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNegativeCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Negative.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression negativeValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(0L)).build();
        return negativeValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getPositiveCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Positive.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression positiveValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLNumberLiteralBuilder.buildWithValue(0L)).build();
        return positiveValueExpression;
    }
    
    @Pure
    public static @Nullable SQLBooleanExpression getCheck(@Nonnull CustomField customField) {
        final @Nullable SQLBooleanExpression multipleOfCheck = getMultipleOfCheck(customField);
        final @Nullable SQLBooleanExpression nonNegativeCheck = getNonNegativeCheck(customField);
        final @Nullable SQLBooleanExpression nonPositiveCheck = getNonPositiveCheck(customField);
        final @Nullable SQLBooleanExpression positiveCheck = getPositiveCheck(customField);
        final @Nullable SQLBooleanExpression negativeCheck = getNegativeCheck(customField);
        @Nullable SQLBooleanExpression result = null;
        if (multipleOfCheck != null) {
            result = multipleOfCheck;
        }
        if (nonNegativeCheck != null) {
            result = result == null ? nonNegativeCheck : SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(result).withRightExpression(nonNegativeCheck).build();
        }
        if (nonPositiveCheck != null) {
            result = result == null ? nonPositiveCheck : SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(result).withRightExpression(nonPositiveCheck).build();
        }
        if (negativeCheck != null) {
            result = result == null ? nonPositiveCheck : SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(result).withRightExpression(negativeCheck).build();
        }
        if (positiveCheck != null) {
            result = result == null ? nonPositiveCheck : SQLBinaryBooleanExpressionBuilder.withOperator(SQLBinaryBooleanOperator.AND).withLeftExpression(result).withRightExpression(positiveCheck).build();
        }
        return result;
    }
    
    @Pure
    public static <@Unspecifiable TYPE> void fillColumnNames(@Nonnull Converter<TYPE, ?> converter, @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columnNames) {
        final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
        for (@Nonnull CustomField field : fields) {
            if (!field.getCustomType().isCompositeType()) {
                if (field.getCustomType().isObjectType()) {
                    final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) field.getCustomType();
                    if (!customConverterType.getConverter().isPrimitiveConverter()) {
                        fillColumnNames(customConverterType.getConverter(), columnNames);
                        return;
                    }
                }
                columnNames.add(SQLColumnNameBuilder.withString(field.getName()).build());
            } else {
                throw new UnsupportedOperationException("Composite types such as iterables or maps are currently not supported by the SQL encoders");
            }
        }
    }
    
    @Pure
    public static @Nonnull SQLQualifiedTable getQualifiedTableName(@Nonnull Converter<?,?> converter, @Nonnull Unit unit) {
        return SQLExplicitlyQualifiedTableBuilder.withTable(SQLTableNameBuilder.withString(converter.getTypeName()).build()).withSchema(SQLSchemaNameBuilder.withString(unit.getName()).build()).build();
    }
    
    @Pure
    public static @Nonnull ImmutableList<SQLTableConstraint> getTableConstraints(@Nonnull Converter<?, ?> converter, @Nonnull Unit unit) {
        final @Nonnull FreezableArrayList<@Nonnull SQLTableConstraint> tableConstraints = FreezableArrayList.withNoElements();
        boolean primaryKeySpecified = false;
        for (@Nonnull CustomField customField : converter.getFields(Representation.INTERNAL)) {
            if (customField.getCustomType().isObjectType()) {
                final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) customField.getCustomType();
                final @Nonnull Class<?> referencedType = customConverterType.getConverter().getType();
                if (Subject.class.isAssignableFrom(referencedType)) {
                    final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columnNames = FreezableArrayList.withNoElements();
                    fillColumnNames(customConverterType.getConverter(), columnNames);
                    final @Nonnull SQLReference reference = SQLReferenceBuilder.withTable(getQualifiedTableName(customConverterType.getConverter(), unit)).withColumns(ImmutableList.withElementsOf(columnNames)).build();
                    final @Nonnull SQLForeignKeyConstraintBuilder.@Nonnull InnerSQLForeignKeyConstraintBuilder sqlForeignKeyConstraintBuilder = SQLForeignKeyConstraintBuilder.withColumns(ImmutableList.withElementsOf(columnNames)).withReference(reference);
                    if (referencedType.isAnnotationPresent(ForeignKey.class)) {
                        final @Nonnull ForeignKey foreignKeyAnnotation = referencedType.getAnnotation(ForeignKey.class);
                        sqlForeignKeyConstraintBuilder.withOnDeleteAction(foreignKeyAnnotation.onDelete()).withOnUpdateAction(foreignKeyAnnotation.onUpdate());
                    } else {
                        sqlForeignKeyConstraintBuilder.withOnDeleteAction(ForeignKeyAction.RESTRICT).withOnUpdateAction(ForeignKeyAction.RESTRICT);
                    }
                    final @Nonnull SQLForeignKeyConstraint foreignKeyConstraint = sqlForeignKeyConstraintBuilder.build();
                    tableConstraints.add(foreignKeyConstraint);
                }
            } else if (isPrimaryKey(customField)) {
                primaryKeySpecified = true;
            }
        }
        if (!primaryKeySpecified) {
            final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columnNames = FreezableArrayList.withNoElements();
            fillColumnNames(converter, columnNames);
            final @Nonnull SQLPrimaryKeyConstraint primaryKeyConstraint = SQLPrimaryKeyConstraintBuilder.withColumns(ImmutableList.withElementsOf(columnNames)).build();
            tableConstraints.add(primaryKeyConstraint);
        }
        return ImmutableList.withElementsOf(tableConstraints);
    }

//    /**
//     * Transforms a list of annotations into a list of column definitions.
//     */
//    @Pure
//    public static @Nonnull @Frozen ReadOnlyList<@Nonnull SQLColumnDefinition> of(@Nonnull ImmutableList<CustomAnnotation> annotations) {
//        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDefinition> columnConstraints = FreezableArrayList.withNoElements();
//        for (@Nonnull CustomAnnotation annotation : annotations) {
//            if (annotation.getAnnotationType().equals(Nonnull.class)) {
//                columnConstraints.add(new SQLNotNullConstraint());
//            } else if (annotation.getAnnotationType().equals(Default.class)) {
//                columnConstraints.add(new SQLDefaultValueConstraint(annotation));
//            }
//        }
//        return columnConstraints.freeze();
//    }
//     
//    /**
//     * Returns a list of column constrains derived from a list of annotations.
//     */
//    @Pure
//    public static @Nonnull ReadOnlyList<@Nonnull SQLColumnConstraint> of(@Nonnull ImmutableList<@Nonnull CustomAnnotation> annotations, @Nonnull String columnName) {
//        final @Nonnull FreezableArrayList<@Nonnull SQLColumnConstraint> columnConstraints = FreezableArrayList.withNoElements();
//        for (@Nonnull CustomAnnotation annotation : annotations) {
//            if (annotation.getAnnotationType().equals(Unique.class)) {
//                columnConstraints.add(new SQLUniqueConstraint());
//            } else if (annotation.getAnnotationType().equals(PrimaryKey.class)) {
//                columnConstraints.add(new SQLPrimaryKeyConstraint());
//            } else if (annotation.getAnnotationType().equals(ForeignKey.class)) {
//                columnConstraints.add(new SQLForeignKeyConstraint(annotation));
//            } else if (annotation.getAnnotationType().equals(MultipleOf.class)) {
//                columnConstraints.add(new SQLCheckMultipleOfConstraint(annotation, columnName));
//            } else if (annotation.getAnnotationType().equals(Negative.class)) {
//                columnConstraints.add(new SQLCheckNegativeConstraint(annotation, columnName));
//            } else if (annotation.getAnnotationType().equals(NonNegative.class)) {
//                columnConstraints.add(new SQLCheckNonNegativeConstraint(annotation, columnName));
//            } else if (annotation.getAnnotationType().equals(Positive.class)) {
//                columnConstraints.add(new SQLCheckPositiveConstraint(annotation, columnName));
//            } else if (annotation.getAnnotationType().equals(NonPositive.class)) {
//                columnConstraints.add(new SQLCheckNonPositiveConstraint(annotation, columnName));
//            }
//        }
//        return columnConstraints;
//    }
// 
}
