package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.annotations.Default;
import net.digitalid.database.dialect.annotations.PrimaryKey;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.annotations.Unique;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 *
 */
public abstract class SQLColumnDefinition implements SQLNode<SQLColumnDefinition> {
    
    public abstract void getColumnDefinition(@Nonnull @NonCapturable StringBuilder string) throws InternalException;
    
    public static @Nonnull @NonNullableElements FreezableList<SQLColumnDefinition> of(@Nonnull Field field) {
        final @Nonnull @NonNullableElements FreezableArrayList<SQLColumnDefinition> columnConstraints = FreezableArrayList.get();
        for (@Nonnull Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().equals(Nonnull.class)) {
                columnConstraints.add(new SQLNotNullConstraint());
            } else if (annotation.annotationType().equals(Default.class)) {
                columnConstraints.add(new SQLDefaultValueConstraint((Default) annotation));
            }
        }
        return columnConstraints;
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDefinition> transcriber = new Transcriber<SQLColumnDefinition>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDefinition node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            node.getColumnDefinition(string);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLColumnDefinition> getTranscriber() {
        return transcriber;
    }
    
}
