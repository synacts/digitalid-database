package net.digitalid.database.dialect.ast.statement.table.create;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.annotations.Default;
import net.digitalid.database.dialect.annotations.PrimaryKey;
import net.digitalid.database.dialect.annotations.References;
import net.digitalid.database.dialect.annotations.Unique;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.math.MultipleOf;
import net.digitalid.utility.validation.math.Negative;
import net.digitalid.utility.validation.math.NonNegative;
import net.digitalid.utility.validation.math.NonPositive;
import net.digitalid.utility.validation.math.Positive;
import net.digitalid.utility.validation.reference.NonCapturable;

/**
 * Description.
 */
public abstract class SQLColumnConstraint implements SQLNode<SQLColumnConstraint> {
    
    public abstract void getConstraintDeclaration(@Nonnull SQLDialect dialect, @Nonnull SQLColumnConstraint node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException;
    
    public static @Nullable SQLColumnConstraint of(@Nonnull Annotation annotation, @Nonnull Field field) {
        if (annotation.annotationType().equals(Unique.class)) {
            return new SQLUniqueConstraint();
        } else if (annotation.annotationType().equals(Nonnull.class)) {
            return new SQLNotNullConstraint();
        } else if (annotation.annotationType().equals(PrimaryKey.class)) {
            return new SQLPrimaryKeyConstraint();
        } else if (annotation.annotationType().equals(References.class)) {
            return new SQLForeignKeyConstraint((References) annotation);
        } else if (annotation.annotationType().equals(Default.class)) {
            return new SQLDefaultValueConstraint((Default) annotation);
        } else if (annotation.annotationType().equals(MultipleOf.class)) {
            return new SQLCheckMultipleOfConstraint(field);
        } else if (annotation.annotationType().equals(Negative.class)) {
            return new SQLCheckNegativeConstraint(field);
        } else if (annotation.annotationType().equals(NonNegative.class)) {
            return new SQLCheckNonNegativeConstraint(field);
        } else if (annotation.annotationType().equals(Positive.class)) {
            return new SQLCheckPositiveConstraint(field);
        } else if (annotation.annotationType().equals(NonPositive.class)) {
            return new SQLCheckNonPositiveConstraint(field);
        }
        return null;
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
