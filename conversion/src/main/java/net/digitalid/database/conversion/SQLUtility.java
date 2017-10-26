package net.digitalid.database.conversion;

import java.sql.Types;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.state.Modifiable;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.FreezableLinkedList;
import net.digitalid.utility.collections.list.FreezableList;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.Log;
import net.digitalid.utility.storage.Table;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.math.Negative;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.NonPositive;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Utility;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.constraints.Unique;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.SQLNullLiteral;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.bool.SQLComparisonOperator;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpression;
import net.digitalid.database.dialect.expression.bool.SQLNumberComparisonBooleanExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberExpressionBuilder;
import net.digitalid.database.dialect.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.dialect.expression.number.SQLDoubleLiteralBuilder;
import net.digitalid.database.dialect.expression.number.SQLLongLiteralBuilder;
import net.digitalid.database.dialect.expression.string.SQLStringLiteralBuilder;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.dialect.identifier.column.SQLColumnNameBuilder;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaName;
import net.digitalid.database.dialect.identifier.schema.SQLSchemaNameBuilder;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLExplicitlyQualifiedTableBuilder;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.dialect.identifier.table.SQLTableName;
import net.digitalid.database.dialect.identifier.table.SQLTableNameBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclaration;
import net.digitalid.database.dialect.statement.table.create.SQLColumnDeclarationBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLReference;
import net.digitalid.database.dialect.statement.table.create.SQLReferenceBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLReferenceOptionBuilder;
import net.digitalid.database.dialect.statement.table.create.SQLTypeBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraint;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLForeignKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLPrimaryKeyConstraint;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLPrimaryKeyConstraintBuilder;
import net.digitalid.database.dialect.statement.table.create.constraints.SQLTableConstraint;
import net.digitalid.database.interfaces.encoder.SQLEncoder;

/**
 * This class helps to convert objects to SQL.
 */
@Utility
public abstract class SQLUtility {
    
    /* -------------------------------------------------- Not Null -------------------------------------------------- */
    
    /**
     * Returns true if the given field is annotated with {@link Nonnull}.
     */
    @Pure
    public static boolean isNotNull(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(Nonnull.class);
    }
    
    /* -------------------------------------------------- Primary Key -------------------------------------------------- */
    
    /**
     * Returns true if the given field is annotated with {@link PrimaryKey}.
     */
    @Pure
    public static boolean isPrimaryKey(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(PrimaryKey.class);
    }
    
    /**
     * Returns true if the given field translates to multiple columns.
     */
    @Pure
    private static boolean consistsOfMultipleColumns(@Nonnull CustomField customField) {
        final @Nonnull CustomType fieldType = customField.getCustomType();
        if (fieldType.isObjectType()) {
            final @Nonnull CustomType.CustomConverterType converterType = (CustomType.CustomConverterType) fieldType;
            if (converterType.getConverter().getFields(Representation.INTERNAL).size() > 1) {
                return true;
            } else {
                return consistsOfMultipleColumns(converterType.getConverter().getFields(Representation.INTERNAL).getFirst());
            }
        } else {
            return false;
        }
    }
    
    /**
     * Returns true if the type has multiple fields annotated with {@link PrimaryKey}.
     */
    @Pure
    public static boolean hasMultiplePrimaryKeys(@Nonnull Converter<?, ?> converter) {
        boolean hasOne = false;
        for (@Nonnull CustomField customField : converter.getFields(Representation.INTERNAL)) {
            if (isPrimaryKey(customField)) {
                if (hasOne || consistsOfMultipleColumns(customField)) {
                    return true;
                }
                hasOne = true;
            }
        }
        return false;
    }
    
    /* -------------------------------------------------- Unique -------------------------------------------------- */
    
    /**
     * Returns true if the given field is annotated with {@link Unique}.
     */
    @Pure
    public static boolean isUnique(@Nonnull CustomField customField) {
        return customField.isAnnotatedWith(Unique.class);
    }
    
    /* -------------------------------------------------- Embedded Primitive Type -------------------------------------------------- */
    
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
        if ("null".equals(defaultValue)) {
            return SQLNullLiteral.INSTANCE;
        }
        @Nullable CustomType customType = customField.getCustomType();
        if (customType.isObjectType()) {
            customType = getEmbeddedPrimitiveType(customType);
        }
        if (customType != null) {
            final int sqlType = SQLEncoder.getSQLType(customType);
            switch (sqlType) {
                case Types.BOOLEAN:
                    if (Boolean.parseBoolean(defaultValue)) {
                        return SQLBooleanLiteral.TRUE;
                    } else {
                        return SQLBooleanLiteral.FALSE;
                    }
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                case Types.BIGINT:
                    try {
                        return SQLLongLiteralBuilder.withValue(Long.parseLong(defaultValue)).build();
                    } catch (@Nonnull NumberFormatException exception) {
                        return null;
                    }
                case Types.FLOAT:
                case Types.DOUBLE:
                    try {
                        return SQLDoubleLiteralBuilder.withValue(Double.parseDouble(defaultValue)).build();
                    } catch (@Nonnull NumberFormatException exception) {
                        return null;
                    }
                case Types.VARCHAR:
                case Types.CHAR:
                    return SQLStringLiteralBuilder.withString(defaultValue).build();
                default:
                    return null;
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
                        .withRightExpression(SQLLongLiteralBuilder.withValue(multipleOfValue).build()).build())
                .withRightExpression(SQLLongLiteralBuilder.withValue(0).build()).build();
        return multipleOfValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNonNegativeCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(NonNegative.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression nonNegativeValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER_OR_EQUAL)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLLongLiteralBuilder.withValue(0).build()).build();
        return nonNegativeValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNonPositiveCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(NonPositive.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression nonPositiveValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS_OR_EQUAL)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLLongLiteralBuilder.withValue(0).build()).build();
        return nonPositiveValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getNegativeCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Negative.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression negativeValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.LESS)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLLongLiteralBuilder.withValue(0).build()).build();
        return negativeValueExpression;
    }
    
    @Pure
    private static @Nullable SQLBooleanExpression getPositiveCheck(@Nonnull CustomField customField) {
        if (!customField.isAnnotatedWith(Positive.class)) { return null; }
        final @Nonnull SQLNumberComparisonBooleanExpression positiveValueExpression = SQLNumberComparisonBooleanExpressionBuilder.withOperator(SQLComparisonOperator.GREATER)
                .withLeftExpression(SQLColumnNameBuilder.withString(customField.getName()).build())
                .withRightExpression(SQLLongLiteralBuilder.withValue(0).build()).build();
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
    
    /* -------------------------------------------------- Column Declarations -------------------------------------------------- */
    
    /**
     * Given a converter, and some information about the nullness and prefix of the fields of the converter, this method fills the given column declarations list with column declarations derived from the converter.
     */
    @Pure
    public static <@Unspecifiable TYPE> void fillColumnDeclarations(@Nonnull Converter<TYPE, ?> converter, @Nonnull @Modified FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations, boolean mustBeNullable, boolean mustBePrimaryKey, boolean converterHasMultiplePrimaryKeys, boolean firstLevel, @Nonnull String prefix) {
        final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
        final boolean multiplePrimaryKeys = firstLevel && (converterHasMultiplePrimaryKeys || hasMultiplePrimaryKeys(converter));
        for (@Nonnull CustomField field : fields) {
            @Nonnull CustomType customType = field.getCustomType();
            if (!customType.isCompositeType()) {
                boolean primitive = true;
                if (customType.isObjectType()) {
                    final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) customType;
                    if (!customConverterType.getConverter().isPrimitiveConverter()) {
                        fillColumnDeclarations(customConverterType.getConverter(), columnDeclarations, mustBeNullable || !SQLUtility.isNotNull(field), (mustBePrimaryKey || (!multiplePrimaryKeys && SQLUtility.isPrimaryKey(field))), multiplePrimaryKeys, false, prefix + field.getName().toLowerCase() + "_");
                        continue;
                    } else { // otherwise we have a boxed primitive type
                        @Nullable CustomType primitiveType = SQLUtility.getEmbeddedPrimitiveType(customConverterType);
                        Require.that(primitiveType != null).orThrow("The custom converter type $ is expected to embed a primitive type", customConverterType);
                        assert primitiveType != null; // suppress compiler warning
                        customType = primitiveType;
                        primitive = false;
                    }
                }
                final @Nonnull SQLColumnDeclaration sqlColumnDeclaration = SQLColumnDeclarationBuilder
                        .withName(SQLColumnNameBuilder.withString(prefix + field.getName()).build())
                        .withType(SQLTypeBuilder.withType(customType).build())
                        .withNotNull(!mustBeNullable && (primitive || SQLUtility.isNotNull(field)))
                        .withDefaultValue(SQLUtility.getDefaultValue(field))
                        .withPrimaryKey((mustBePrimaryKey || firstLevel && (!multiplePrimaryKeys && SQLUtility.isPrimaryKey(field))))
                        .withUnique(SQLUtility.isUnique(field))
                        .withCheck(SQLUtility.getCheck(field))
                        .build();
                columnDeclarations.add(sqlColumnDeclaration);
            } else {
                throw new UnsupportedOperationException("Composite types such as iterables or maps are currently not supported by the SQL encoders");
            }
        }
    }
    
    /**
     * Returns an immutable list of column declarations for a given converter.
     */
    @Pure
    public static <@Unspecifiable TYPE> @Nonnull @NonEmpty ImmutableList<@Nonnull SQLColumnDeclaration> getColumnDeclarations(@Nonnull Converter<TYPE, ?> converter) {
        final @Nonnull @Modifiable FreezableArrayList<@Nonnull SQLColumnDeclaration> columnDeclarations = FreezableArrayList.withNoElements();
        SQLUtility.fillColumnDeclarations(converter, columnDeclarations, false, false, false, true, "");
        return ImmutableList.withElementsOf(columnDeclarations);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    /**
     * Given a converter, and some information about the prefix of the fields of the converter, this method fills the given column name list with column names derived from the converter.
     */
    @Pure
    public static <@Unspecifiable TYPE> void fillColumnNames(@Nonnull Converter<TYPE, ?> converter, @Nonnull FreezableList<@Nonnull SQLColumnName> columnNames, @Nonnull String prefix) {
        final @Nonnull ImmutableList<@Nonnull CustomField> fields = converter.getFields(Representation.INTERNAL);
        for (@Nonnull CustomField field : fields) {
            if (!field.getCustomType().isCompositeType()) {
                final @Nonnull String fieldName;
                if (converter.isPrimitiveConverter()) {
                    Require.that(!prefix.isEmpty()).orThrow("The primitive converter $ requires a prefix.", converter);
                    Require.that(!field.getCustomType().isObjectType()).orThrow("The primitive converter $ has a non-primitive field $", converter, field.getName());
                    fieldName = prefix;
                } else {
                    fieldName = prefix.isEmpty() ? field.getName() : prefix + "_" + field.getName();
                    if (field.getCustomType().isObjectType()) {
                        final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) field.getCustomType();
                        if (!customConverterType.getConverter().isPrimitiveConverter()) {
                            fillColumnNames(customConverterType.getConverter(), columnNames, fieldName);
                            continue;
                        }
                    }
                }
                columnNames.add(SQLColumnNameBuilder.withString(fieldName.toLowerCase()).build());
            } else {
                throw new UnsupportedOperationException("Composite types such as iterables or maps are currently not supported by the SQL encoders");
            }
        }
    }
    
    /**
     * Returns an immutable list of column names for the given converter. A non-null prefix can be specified.
     */
    @Pure
    public static @Nonnull @NonNegative ImmutableList<@Nonnull SQLColumnName> getColumnNames(@Nonnull Converter<?, ?> converter, @Nonnull String prefix) {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnName> columnNames = FreezableArrayList.withNoElements();
        fillColumnNames(converter, columnNames, prefix);
        return ImmutableList.withElementsOf(columnNames);
    }
    
    /**
     * Returns an immutable list of column names for the given converter.
     */
    @Pure
    public static @Nonnull @NonNegative ImmutableList<@Nonnull SQLColumnName> getColumnNames(@Nonnull Converter<?, ?> converter) {
        return getColumnNames(converter, "");
    }
    
    /* -------------------------------------------------- Qualified Table Name -------------------------------------------------- */
    
    /**
     * Returns the qualified table name for a given table on a given unit.
     */
    @Pure
    public static @Nonnull SQLQualifiedTable getQualifiedTableName(@Nonnull Table<?, ?> table, @Nonnull Unit unit) {
        final @Nonnull SQLTableName tableName = SQLTableNameBuilder.withString(table.getTableName(unit)).build();
        final @Nonnull SQLSchemaName schemaName = SQLSchemaNameBuilder.withString(table.getSchemaName(unit)).build();
        return SQLExplicitlyQualifiedTableBuilder.withTable(tableName).withSchema(schemaName).build();
    }
    
    /* -------------------------------------------------- Table Constraints -------------------------------------------------- */
    
    /**
     * Returns the table constraints for a given converter and a given unit.
     * Foreign key constraints and primary key constraints are supported.
     * If the converter represents a table with a foreign key that references another table in another schema, the name of the unit is taken
     * from the type annotation. In other cases, the name of the unit is taken from the given unit object.
     */
    @Pure
    public static @Nonnull ImmutableList<SQLTableConstraint> getTableConstraints(@Nonnull Table<?, ?> tableConverter, @Nonnull Unit unit) {
        final @Nonnull FreezableList<@Nonnull SQLTableConstraint> tableConstraints = FreezableLinkedList.withNoElements();
        boolean primaryKeySpecified = false;
        final boolean multiplePrimaryKeys = hasMultiplePrimaryKeys(tableConverter);
        final @Nonnull FreezableList<@Nonnull SQLColumnName> primaryKeyColumns = FreezableLinkedList.withNoElements();
        for (@Nonnull CustomField customField : tableConverter.getFields(Representation.INTERNAL)) {
            final @Nonnull CustomType fieldType = customField.getCustomType();
            if (fieldType.isObjectType()) {
                final @Nonnull CustomType.CustomConverterType customConverterType = (CustomType.CustomConverterType) fieldType;
                final @Nonnull Converter<?, ?> referenceConverter = customConverterType.getConverter();
                if (referenceConverter instanceof Table<?, ?>) {
                    final @Nonnull Table<?, ?> table = (Table<?, ?>) referenceConverter;
                    if (!table.getTableName(unit).equals(tableConverter.getTableName(unit))) { // TODO: Is this the best way to avoid self-references in core subject tables?
                        final @Nonnull @NonNullableElements ImmutableList<SQLColumnName> columnNames = ImmutableList.withElementsOf(table.getColumnNames(unit).map(columnName -> SQLColumnNameBuilder.withString(columnName).build()));
                        if (!columnNames.isEmpty()) {
                            final @Nonnull SQLExplicitlyQualifiedTable qualifiedTable = SQLExplicitlyQualifiedTableBuilder.withTable(SQLTableNameBuilder.withString(table.getTableName(unit)).build()).withSchema(SQLSchemaNameBuilder.withString(table.getSchemaName(unit)).build()).build();
                            final @Nonnull SQLReference reference = SQLReferenceBuilder.withTable(qualifiedTable).withColumns(columnNames).withDeleteOption(SQLReferenceOptionBuilder.withAction(table.getOnDeleteAction()).build()).withUpdateOption(SQLReferenceOptionBuilder.withAction(table.getOnUpdateAction()).build()).build();
                            final @Nonnull SQLForeignKeyConstraint foreignKeyConstraint = SQLForeignKeyConstraintBuilder.withColumns(getColumnNames(referenceConverter, customField.getName().toLowerCase())).withReference(reference).build();
                            tableConstraints.add(foreignKeyConstraint);
                        }
                    }
                }
                // TODO: Remove the old implementation as soon as it is no longer needed.
//                final @Nonnull Class<?> referencedType = referenceConverter.getType();
//                if (Subject.class.isAssignableFrom(referencedType)) {
//                    final @Nonnull ImmutableList<@Nonnull SQLColumnName> referencedColumnNames = getColumnNames(referenceConverter);
//                    final @Nonnull ImmutableList<@Nonnull SQLColumnName> columnNames = getColumnNames(referenceConverter, customField.getName().toLowerCase());
//                    if (columnNames.size() > 0) {
//                        final @Nonnull String unitName;
//                        if (referencedType.isAnnotationPresent(UnitName.class)) {
//                            unitName = referencedType.getAnnotation(UnitName.class).value();
//                        } else {
//                            unitName = unit.getName();
//                        }
//                        final @Nonnull SQLReference reference = SQLReferenceBuilder.withTable(getQualifiedTableName(referenceConverter, unitName)).withColumns(referencedColumnNames).build();
//                        final @Nonnull SQLForeignKeyConstraintBuilder.@Nonnull InnerSQLForeignKeyConstraintBuilder sqlForeignKeyConstraintBuilder = SQLForeignKeyConstraintBuilder.withColumns(columnNames).withReference(reference);
//                        if (referencedType.isAnnotationPresent(ForeignKey.class)) {
//                            final @Nonnull ForeignKey foreignKeyAnnotation = referencedType.getAnnotation(ForeignKey.class);
//                            sqlForeignKeyConstraintBuilder.withOnDeleteAction(foreignKeyAnnotation.onDelete()).withOnUpdateAction(foreignKeyAnnotation.onUpdate());
//                        } else {
//                            sqlForeignKeyConstraintBuilder.withOnDeleteAction(ForeignKeyAction.RESTRICT).withOnUpdateAction(ForeignKeyAction.RESTRICT);
//                        }
//                        final @Nonnull SQLForeignKeyConstraint foreignKeyConstraint = sqlForeignKeyConstraintBuilder.build();
//                        tableConstraints.add(foreignKeyConstraint);
//                    }
//                }
            } 
            if (isPrimaryKey(customField)) {
                primaryKeySpecified = true;
                if (multiplePrimaryKeys) {
                    if (fieldType.isObjectType()) {
                        final @Nonnull Converter<?, ?> fieldTypeConverter = ((CustomType.CustomConverterType) fieldType).getConverter();
                        if (!fieldTypeConverter.isPrimitiveConverter()) {
                            fillColumnNames(fieldTypeConverter, primaryKeyColumns, customField.getName().toLowerCase());
                        } else {
                            primaryKeyColumns.add(SQLColumnNameBuilder.withString(customField.getName()).build());
                        }
                    } else {
                        primaryKeyColumns.add(SQLColumnNameBuilder.withString(customField.getName()).build());
                    }
                }
            }
        }
        if (!primaryKeyColumns.isEmpty()) {
            final @Nonnull ImmutableList<@Nonnull SQLColumnName> columnNames = ImmutableList.withElementsOf(primaryKeyColumns);
            final @Nonnull SQLPrimaryKeyConstraint primaryKeyConstraint = SQLPrimaryKeyConstraintBuilder.withColumns(columnNames).build();
            tableConstraints.add(primaryKeyConstraint);
        }
        if (!primaryKeySpecified) {
            final @Nonnull ImmutableList<@Nonnull SQLColumnName> columnNames = getColumnNames(tableConverter);
            final @Nonnull SQLPrimaryKeyConstraint primaryKeyConstraint = SQLPrimaryKeyConstraintBuilder.withColumns(columnNames).build();
            tableConstraints.add(primaryKeyConstraint);
        }
        return ImmutableList.withElementsOf(tableConstraints);
    }

}
