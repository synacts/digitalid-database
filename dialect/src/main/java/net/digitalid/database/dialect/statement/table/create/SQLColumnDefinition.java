package net.digitalid.database.dialect.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.generation.Default;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents a column definition.
 */
public abstract class SQLColumnDefinition implements SQLNode<SQLColumnDefinition> {
    
    /**
     * Stores a string representation of the specific column definition in the string builder.
     */
    @Pure
    public abstract void getColumnDefinition(@Nonnull @NonCaptured @Modified StringBuilder string) throws InternalException;
    
    /**
     * Transforms a list of annotations into a list of column definitions.
     */
    @Pure
    public static @Nonnull @Frozen ReadOnlyList<@Nonnull SQLColumnDefinition> of(@Nonnull ImmutableList<CustomAnnotation> annotations) {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDefinition> columnConstraints = FreezableArrayList.withNoElements();
        for (@Nonnull CustomAnnotation annotation : annotations) {
            if (annotation.getAnnotationType().equals(Nonnull.class)) {
                columnConstraints.add(new SQLNotNullConstraint());
            } else if (annotation.getAnnotationType().equals(Default.class)) {
                columnConstraints.add(new SQLDefaultValueConstraint(annotation));
            }
        }
        return columnConstraints.freeze();
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that return a string representation of this SQL node.
     */
    private static final @Nonnull Transcriber<SQLColumnDefinition> transcriber = new Transcriber<SQLColumnDefinition>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDefinition node, @Nonnull Site site) throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            node.getColumnDefinition(string);
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLColumnDefinition> getTranscriber() {
        return transcriber;
    }
    
}
