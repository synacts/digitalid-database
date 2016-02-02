package net.digitalid.database.dialect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.annotation.Nonnull;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface References {
    
    @Nonnull String foreignTable();
    
    @Nonnull @NonNullableElements String[] columnNames();
    
    enum Action {
        RESTRICT("RESTRICT"), 
        CASCADE("CASCADE"), 
        SET_NULL("SET NULL"), 
        NO_ACTION("UPDATE");
    
        public final @Nonnull String value;
        
        Action(@Nonnull String value) {
            this.value = value;
        }
    };
    
    @Nonnull Action onDelete() default Action.RESTRICT;
    @Nonnull Action onUpdate() default Action.RESTRICT;
    
}
