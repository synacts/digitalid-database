package net.digitalid.database.dialect.annotations;

import javax.annotation.Nonnull;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;

/**
 *
 */
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
