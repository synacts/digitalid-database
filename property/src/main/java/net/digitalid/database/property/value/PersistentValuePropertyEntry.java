package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.PersistentPropertyEntry;
import net.digitalid.database.property.subject.Subject;

/**
 * This class models an entry in the {@link PersistentValuePropertyTable persistent value property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentValuePropertyEntry<@Unspecifiable SUBJECT extends Subject<?>, @Specifiable VALUE> extends PersistentPropertyEntry<SUBJECT> {
    
    /* -------------------------------------------------- Time -------------------------------------------------- */
    
    /**
     * Returns the time of the last modification.
     */
    @Pure
    public abstract @Nonnull Time getTime();
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value of the property.
     */
    @Pure
    public abstract VALUE getValue();
    
}
