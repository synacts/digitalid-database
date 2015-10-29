package net.digitalid.utility.database.column;

import java.sql.SQLException;
import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Stateless;
import net.digitalid.utility.database.annotations.Locked;
import net.digitalid.utility.database.annotations.NonCommitting;
import net.digitalid.utility.database.site.Site;

/**
 * This class allows to retrieve foreign key references that are {@link Site site}-independent.
 */
@Stateless
public abstract class GeneralReference extends Reference {
    
    /**
     * Returns the string used to reference to an instance of the referenceable class.
     * 
     * @return the string used to reference to an instance of the referenceable class.
     * 
     * @ensure return.startsWith("REFERENCES") : "The returned string is a reference.";
     */
    @Locked
    @NonCommitting
    public abstract @Nonnull String get() throws SQLException;
    
    @Locked
    @Override
    @NonCommitting
    public final @Nonnull String get(@Nonnull Site site) throws SQLException {
        return get();
    }
    
}
