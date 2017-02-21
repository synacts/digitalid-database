package net.digitalid.database.annotations.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;

import net.digitalid.database.enumerations.ForeignKeyAction;

/**
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
    
    @Nonnull ForeignKeyAction onDelete() default ForeignKeyAction.RESTRICT;
    
    @Nonnull ForeignKeyAction onUpdate() default ForeignKeyAction.RESTRICT;
    
}
