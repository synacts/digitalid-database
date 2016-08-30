package net.digitalid.database.property.simple;

import net.digitalid.utility.property.simple.WritableSimpleProperty;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;

/**
 * A persistent writable property stores a simple value in the database.
 * 
 * @see SimpleObjectProperty
 */
@Mutable
public abstract class PersistentWritableSimpleProperty<V> extends WritableSimpleProperty<V, DatabaseException> implements PersistentProperty {}
