package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.subject.Subject;
import net.digitalid.database.property.value.PersistentValuePropertyEntry;

/**
 * This class models an entry in the {@link PersistentPropertyTable persistent property table}.
 * 
 * @see PersistentValuePropertyEntry
 */
@Immutable
public abstract class PersistentPropertyEntry<@Unspecifiable SUBJECT extends Subject<?>> extends RootClass {
    
    /* -------------------------------------------------- Subject -------------------------------------------------- */
    
    /**
     * Returns the subject to which the property belongs.
     */
    @Pure
    @PrimaryKey
    public abstract @Nonnull SUBJECT getSubject();
    
}
