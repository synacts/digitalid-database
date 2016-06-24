package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.math.Negative;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.NonPositive;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.PrimaryKey;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.annotations.Unique;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This SQL node represents an SQL column constraint.
 */
public abstract class SQLColumnConstraint implements SQLParameterizableNode<SQLColumnConstraint> {
    
    /**
     * Returns the constraint declaration for the specific column constraint.
     */
    @Pure
    public abstract @Nonnull String getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException;
    
    /**
     * Returns a list of column constrains derived from a list of annotations.
     */
    @Pure
    public static @Nonnull ReadOnlyList<@Nonnull SQLColumnConstraint> of(@Nonnull ImmutableList<@Nonnull Annotation> annotations, @Nonnull String columnName) {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnConstraint> columnConstraints = FreezableArrayList.withNoElements();
        for (@Nonnull Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Unique.class)) {
                columnConstraints.add(new SQLUniqueConstraint());
            } else if (annotation.annotationType().equals(PrimaryKey.class)) {
                columnConstraints.add(new SQLPrimaryKeyConstraint());
            } else if (annotation.annotationType().equals(References.class)) {
                columnConstraints.add(new SQLForeignKeyConstraint((References) annotation));
            } else if (annotation.annotationType().equals(MultipleOf.class)) {
                columnConstraints.add(new SQLCheckMultipleOfConstraint(annotation, columnName));
            } else if (annotation.annotationType().equals(Negative.class)) {
                columnConstraints.add(new SQLCheckNegativeConstraint(annotation, columnName));
            } else if (annotation.annotationType().equals(NonNegative.class)) {
                columnConstraints.add(new SQLCheckNonNegativeConstraint(annotation, columnName));
            } else if (annotation.annotationType().equals(Positive.class)) {
                columnConstraints.add(new SQLCheckPositiveConstraint(annotation, columnName));
            } else if (annotation.annotationType().equals(NonPositive.class)) {
                columnConstraints.add(new SQLCheckNonPositiveConstraint(annotation, columnName));
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
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site) throws InternalException {
            return node.getConstraintDeclaration(dialect, node, site);
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLColumnConstraint> getTranscriber() {
        return transcriber;
    }
    
}
