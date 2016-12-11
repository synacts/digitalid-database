package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.math.Negative;
import net.digitalid.utility.validation.annotations.math.NonNegative;
import net.digitalid.utility.validation.annotations.math.NonPositive;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.math.modulo.MultipleOf;

import net.digitalid.database.annotations.constraints.ForeignKey;
import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.constraints.Unique;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.subject.site.Site;

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
    public static @Nonnull ReadOnlyList<@Nonnull SQLColumnConstraint> of(@Nonnull ImmutableList<@Nonnull CustomAnnotation> annotations, @Nonnull String columnName) {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnConstraint> columnConstraints = FreezableArrayList.withNoElements();
        for (@Nonnull CustomAnnotation annotation : annotations) {
            if (annotation.getAnnotationType().equals(Unique.class)) {
                columnConstraints.add(new SQLUniqueConstraint());
            } else if (annotation.getAnnotationType().equals(PrimaryKey.class)) {
                columnConstraints.add(new SQLPrimaryKeyConstraint());
            } else if (annotation.getAnnotationType().equals(ForeignKey.class)) {
                columnConstraints.add(new SQLForeignKeyConstraint(annotation));
            } else if (annotation.getAnnotationType().equals(MultipleOf.class)) {
                columnConstraints.add(new SQLCheckMultipleOfConstraint(annotation, columnName));
            } else if (annotation.getAnnotationType().equals(Negative.class)) {
                columnConstraints.add(new SQLCheckNegativeConstraint(annotation, columnName));
            } else if (annotation.getAnnotationType().equals(NonNegative.class)) {
                columnConstraints.add(new SQLCheckNonNegativeConstraint(annotation, columnName));
            } else if (annotation.getAnnotationType().equals(Positive.class)) {
                columnConstraints.add(new SQLCheckPositiveConstraint(annotation, columnName));
            } else if (annotation.getAnnotationType().equals(NonPositive.class)) {
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
