package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.immutable.ImmutableList;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.annotations.Default;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public abstract class SQLColumnDefinition implements SQLNode<SQLColumnDefinition> {
    
    public abstract void getColumnDefinition(@Nonnull @NonCaptured StringBuilder string) throws InternalException;
    
    public static @Nonnull @Frozen ReadOnlyList<@Nonnull SQLColumnDefinition> of(@Nonnull ImmutableList<Annotation> annotations) {
        final @Nonnull FreezableArrayList<@Nonnull SQLColumnDefinition> columnConstraints = FreezableArrayList.withNoElements();
        for (@Nonnull Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Nonnull.class)) {
                columnConstraints.add(new SQLNotNullConstraint());
            } else if (annotation.annotationType().equals(Default.class)) {
                columnConstraints.add(new SQLDefaultValueConstraint((Default) annotation));
            }
        }
        return columnConstraints.freeze();
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDefinition> transcriber = new Transcriber<SQLColumnDefinition>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDefinition node, @Nonnull Site site, @Nonnull @NonCaptured StringBuilder string, boolean parameterizable) throws InternalException {
            node.getColumnDefinition(string);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLColumnDefinition> getTranscriber() {
        return transcriber;
    }
    
}
