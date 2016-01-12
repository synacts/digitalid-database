package net.digitalid.database.core.converter.key;

import javax.annotation.Nonnull;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.exceptions.external.InvalidEncodingException;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * This interface allows to convert an object to its key and recover it again given its key (and an external object).
 * 
 * @param <O> the type of the objects that this converter can convert and recover (typically the surrounding class).
 * @param <E> the type of the external object that is needed to recover an object, which is quite often an entity.
 *            In case no external information is needed for the recovery of an object, declare it as an object.
 * @param <K> the type of the keys which the objects are converted to and recovered from (with an external object).
 * @param <D> the type of the external object that is needed to recover the key, which is quite often an entity.
 *            In case no external information is needed for the recovery of the key, declare it as an object.
 */
public interface KeyConverter<O, E, K, D> {
    
    /**
     * Returns whether the given key is valid.
     * 
     * @param key the key to be checked.
     * 
     * @return whether the given key is valid.
     */
    @Pure
    public boolean isValid(@Nonnull K key);
    
    /**
     * Decomposes the external object into what is needed to recover the key.
     * 
     * @param external the external object needed to recover the object.
     * 
     * @return the external object which is needed to recover the key.
     */
    @Pure
    public @Nonnull D decompose(@Nonnull E external);
    
    /**
     * Returns the key of the given object.
     * 
     * @param object the object whose key is to be returned.
     * 
     * @return the key of the given object.
     */
    @Pure
    public @Nonnull @Validated K convert(@Nonnull O object);
    
    /**
     * Returns the object with the given key.
     * 
     * @param external the external object needed to recover the object.
     * @param key the key which denotes the returned object.
     * 
     * @return the object with the given key.
     */
    @Pure
    public @Nonnull O recover(@Nonnull E external, @Nonnull @Validated K key) throws InvalidEncodingException, InternalException;
    
}
