package net.digitalid.utility.database.column;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.site.Site;

/**
 * This class allows to retrieve foreign key references in a unified way.
 * 
 * @see GeneralReference
 */
@Stateless
public abstract class Reference {
    
    /**
     * Returns whether this reference depends on an entity.
     * 
     * @return whether this reference depends on an entity.
     */
    @Pure
    public abstract boolean isEntityDependent();
    
    /**
     * Returns the string used to reference to an instance of the referenceable class.
     * 
     * @param site the site at which the foreign key constraint is declared and used.
     * 
     * @return the string used to reference to an instance of the referenceable class.
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    @Locked
    @NonCommitting
    public abstract @Nonnull String get(@Nonnull Site site) throws SQLException;
    
}
