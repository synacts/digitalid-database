package net.digitalid.database.enumerations;

import javax.annotation.Nonnull;

/**
 * TODO: Improve type.
 */
public enum ForeignKeyAction {
    
    RESTRICT("RESTRICT"), 
    CASCADE("CASCADE"), 
    SET_NULL("SET NULL"), 
    NO_ACTION("UPDATE");

    public final @Nonnull String value;

    private ForeignKeyAction(@Nonnull String value) {
        this.value = value;
    }
    
}
