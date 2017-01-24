package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.property.set.ReadOnlySetProperty;
import net.digitalid.utility.validation.annotations.type.ReadOnly;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.property.PersistentProperty;
import net.digitalid.database.subject.Subject;

/**
 * This read-only property stores a set of values in the persistent database.
 * 
 * @see WritablePersistentSetPropertyImplementation
 * @see ReadOnlyPersistentSimpleSetProperty
 */
@ThreadSafe
@ReadOnly(WritablePersistentSetPropertyImplementation.class)
public interface ReadOnlyPersistentSetProperty<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE, @Unspecifiable READONLY_SET extends ReadOnlySet<@Nonnull @Valid VALUE>> extends ReadOnlySetProperty<VALUE, READONLY_SET, DatabaseException, RecoveryException, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>, ReadOnlyPersistentSetProperty<SUBJECT, VALUE, READONLY_SET>>, PersistentProperty<SUBJECT, PersistentSetPropertyEntry<SUBJECT, VALUE>, PersistentSetObserver<SUBJECT, VALUE, READONLY_SET>> {
    
    /* -------------------------------------------------- Getter -------------------------------------------------- */
    
    @Pure
    @Override
    @NonCommitting
    public @Nonnull @NonFrozen READONLY_SET get() throws DatabaseException, RecoveryException;
    
    /* -------------------------------------------------- Table -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentSetPropertyTable<?, SUBJECT, VALUE, ?> getTable();
    
}
