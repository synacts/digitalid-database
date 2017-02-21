package net.digitalid.database.property.set;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.PersistentPropertyEntry;
import net.digitalid.database.subject.Subject;

/**
 * This class models an entry in the {@link PersistentSetPropertyTable persistent set property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentSetPropertyEntry<@Unspecifiable SUBJECT extends Subject<?>, @Unspecifiable VALUE> extends PersistentPropertyEntry<SUBJECT> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns a value of the property.
     */
    @Pure
    @PrimaryKey
    public abstract VALUE getValue();
    
}
