package net.digitalid.database.core.converter;

import javax.annotation.Nonnull;
import net.digitalid.database.core.Database;
import net.digitalid.utility.annotations.state.Pure;

/**
 * Objects of classes that implement this interface can be stored in the {@link Database database}.
 * 
 * @param <O> the type of the objects that the converter can store and restore, which is typically the declaring class itself.
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an Entity.
 *            In case no external information is needed for the restoration of an object, declare it as an Object.
 */
public interface SQL<O, E> {
    
    /**
     * Returns the converter to store and restore objects of this class.
     * 
     * @return the converter to store and restore objects of this class.
     */
    @Pure
    public @Nonnull AbstractSQLConverter<O, E> getSQLConverter();
    
}
