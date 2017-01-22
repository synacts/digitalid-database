package net.digitalid.database.dialect.statement.table.create;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.SQLExpression;
import net.digitalid.database.dialect.expression.bool.SQLBooleanExpression;
import net.digitalid.database.dialect.identifier.column.SQLColumnName;
import net.digitalid.database.unit.Unit;

/**
 * This SQL node represents a column declaration.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLColumnDeclaration extends SQLNode {
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    /**
     * Returns the name of the declared column.
     */
    @Pure
    public @Nonnull SQLColumnName getName();
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    /**
     * Returns the type of the declared column.
     */
    @Pure
    public @Nonnull SQLType getType();
    
    /* -------------------------------------------------- Not Null -------------------------------------------------- */
    
    /**
     * Returns whether the declared column is not null.
     */
    @Pure
    @Default("false")
    public boolean isNotNull();
    
    /* -------------------------------------------------- Primary Key -------------------------------------------------- */
    
    /**
     * Returns whether the declared column is a primary key.
     */
    @Pure
    @Default("false")
    public boolean isPrimaryKey();
    
    /* -------------------------------------------------- Auto Increment -------------------------------------------------- */
    
    /**
     * Returns whether the declared column is auto incrementing.
     */
    @Pure
    @Default("false")
    public boolean isAutoIncrement();
    
    /* -------------------------------------------------- Unique -------------------------------------------------- */
    
    /**
     * Returns whether the declared column is unique.
     */
    @Pure
    @Default("false")
    public boolean isUnique();
    
    /* -------------------------------------------------- Check -------------------------------------------------- */
    
    /**
     * Returns the optional check on the declared column.
     */
    @Pure
    public @Nullable SQLBooleanExpression getCheck();
    
    /* -------------------------------------------------- Default Value -------------------------------------------------- */
    
    /**
     * Returns the default value of the declared column.
     */
    @Pure
    public @Nullable SQLExpression getDefaultValue();
    
    /* -------------------------------------------------- Reference -------------------------------------------------- */
    
    /**
     * Returns the optional foreign key reference of the declared column.
     */
    @Pure
    public @Nullable SQLReference getReference();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getName(), unit, string);
        string.append(" ");
        dialect.unparse(getType(), unit, string);
        
        if (isNotNull()) { string.append(" NOT NULL"); }
        if (isPrimaryKey()) { string.append(" PRIMARY KEY"); }
        if (isAutoIncrement()) { string.append(" AUTOINCREMENT"); }
        if (isUnique()) { string.append(" UNIQUE"); }
        
        final @Nullable SQLBooleanExpression check = getCheck();
        if (check != null) {
            string.append(" CHECK (");
            dialect.unparse(check, unit, string);
            string.append(")");
        }
        
        final @Nullable SQLExpression defaultValue = getDefaultValue();
        if (defaultValue != null) {
            string.append(" DEFAULT (");
            dialect.unparse(defaultValue, unit, string);
            string.append(")");
        }
        
        final @Nullable SQLReference reference = getReference();
        if (reference != null) { dialect.unparse(reference, unit, string); }
    }
    
    /* -------------------------------------------------- Utility -------------------------------------------------- */
    
    // TODO: Move the following code to another artifact.
    
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
    
}
