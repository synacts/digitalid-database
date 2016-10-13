package net.digitalid.database.annotations.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.enumerations.ForeignKeyAction;
import net.digitalid.database.enumerations.SQLType;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ForeignKey {
    
    @Nonnull String foreignTable();
    
    @Nonnull SQLType columnType();
    
    @TODO(task = "Shouldn't this rather be an array of column names?", date = "2016-10-13", author = Author.KASPAR_ETTER)
    @Nonnull @NonNullableElements String columnName();
    
    @Nonnull ForeignKeyAction onDelete() default ForeignKeyAction.RESTRICT;
    
    @Nonnull ForeignKeyAction onUpdate() default ForeignKeyAction.RESTRICT;
    
}
