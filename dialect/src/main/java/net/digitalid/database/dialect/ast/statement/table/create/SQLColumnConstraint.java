package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableHashSet;
import net.digitalid.utility.collections.freezable.FreezableSet;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.math.MultipleOf;
import net.digitalid.utility.validation.annotations.math.Negative;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.NonPositive;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.annotations.Default;
import net.digitalid.database.dialect.annotations.PrimaryKey;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.annotations.Unique;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * Description.
 */
public abstract class SQLColumnConstraint implements SQLNode<SQLColumnConstraint> {
    
    public abstract void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException;
    
    
    public static @Nonnull @NonNullableElements FreezableSet<SQLColumnConstraint> of(@Nonnull Field field) {
        final @Nonnull @NonNullableElements FreezableHashSet<SQLColumnConstraint> columnConstraints = FreezableHashSet.get();
        for (@Nonnull Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().equals(Unique.class)) {
                columnConstraints.add(new SQLUniqueConstraint());
            } else if (annotation.annotationType().equals(Nonnull.class)) {
                columnConstraints.add(new SQLNotNullConstraint());
            } else if (annotation.annotationType().equals(PrimaryKey.class)) {
                columnConstraints.add(new SQLPrimaryKeyConstraint());
            } else if (annotation.annotationType().equals(References.class)) {
                columnConstraints.add(new SQLForeignKeyConstraint((References) annotation));
            } else if (annotation.annotationType().equals(Default.class)) {
                columnConstraints.add(new SQLDefaultValueConstraint((Default) annotation));
            } else if (annotation.annotationType().equals(MultipleOf.class)) {
                columnConstraints.add(new SQLCheckMultipleOfConstraint(field));
            } else if (annotation.annotationType().equals(Negative.class)) {
                columnConstraints.add(new SQLCheckNegativeConstraint(field));
            } else if (annotation.annotationType().equals(NonNegative.class)) {
                columnConstraints.add(new SQLCheckNonNegativeConstraint(field));
            } else if (annotation.annotationType().equals(Positive.class)) {
                columnConstraints.add(new SQLCheckPositiveConstraint(field));
            } else if (annotation.annotationType().equals(NonPositive.class)) {
                columnConstraints.add(new SQLCheckNonPositiveConstraint(field));
            }
        }
        return columnConstraints;
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnConstraint> transcriber = new Transcriber<SQLColumnConstraint>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            node.getConstraintDeclaration(dialect, node, site, string);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLColumnConstraint> getTranscriber() {
        return transcriber;
    }
}
