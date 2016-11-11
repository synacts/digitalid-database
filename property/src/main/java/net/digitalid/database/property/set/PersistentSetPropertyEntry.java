package net.digitalid.database.property.set;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.type.Embedded;
import net.digitalid.database.property.PersistentPropertyEntry;
import net.digitalid.database.property.Subject;

/**
 * This class models an entry in the {@link SetPropertyTable set property table}.
 */
@Immutable
@GenerateSubclass
public abstract class PersistentSetPropertyEntry<S extends Subject, V> extends PersistentPropertyEntry<S> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns a value of the property.
     */
    @Pure
    @Embedded
    @PrimaryKey
    @TODO(task = "The embedding is dependent on the actual type (and should cause a foreign key constraint if necessary). Please replace these field/method annotations with type annotations.", date = "2016-09-24", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.MIDDLE)
    public abstract V getValue();
    
}
