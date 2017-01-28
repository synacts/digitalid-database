package net.digitalid.database.conversion.utility;

import java.sql.Types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.constraints.Unique;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.dialect.expression.number.SQLNumberLiteralBuilder;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLReference;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

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
    public static @Nullable SQLExpression getDefaultValue(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Default.class)) { return null; }
        final @Nonnull CustomAnnotation annotation = customField.getAnnotation(Default.class);
        final @Nullable String defaultValue = annotation.get("value", String.class);
    
        final int sqlType = SQLEncoder.getSQLType(customField.getCustomType());
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
    }
    
    @Pure
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
    public static @Nullable SQLBooleanExpression getCheck(@Nonnull CustomField customField) {
        final @Nullable SQLBooleanExpression multipleOfCheck = getMultipleOfCheck(customField);
        if (multipleOfCheck != null) {
            return multipleOfCheck;
        }
        // TODO: negative, non-negative, positive, non-positive checks
        return null;
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
