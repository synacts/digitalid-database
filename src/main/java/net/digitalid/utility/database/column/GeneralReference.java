package net.digitalid.utility.database.column;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.site.Site;

/**
 * This class allows to retrieve foreign key references that are {@link Site site}-independent.
 */
@Stateless
public final class GeneralReference extends Reference {
    
    /* -------------------------------------------------- Entity Dependence -------------------------------------------------- */
    
    @Pure
    @Override
    public boolean isEntityDependent() {
        return false;
    }
    
    /* -------------------------------------------------- String -------------------------------------------------- */
    
    /**
     * Stores the string of this reference.
     */
    private final @Nonnull String string;
    
    /**
     * Returns the string used to reference to an instance of the referenceable class.
     * 
     * @return the string used to reference to an instance of the referenceable class.
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    @Pure
    @Locked
    @NonCommitting
    public @Nonnull String get() {
        return string;
    }
    
    @Locked
    @Override
    @NonCommitting
    public final @Nonnull String get(@Nonnull Site site) throws SQLException {
        return get();
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new general reference with the given string.
     * 
     * @param string the string of the new reference.
     */
    private GeneralReference(@Nonnull String string) {
        this.string = string;
    }
    
    /**
     * Creates a new general reference with the given string.
     * 
     * @param string the string of the new reference.
     * 
     * @return a new general reference with the given string.
     */
    @Pure
    public static @Nonnull GeneralReference get(@Nonnull String string) {
        return new GeneralReference(string);
    }
    
}
