package net.digitalid.database.subject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.rootclass.RootInterface;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.generation.Provided;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.subject.annotations.GenerateSubjectModule;

/**
 * A subject belongs to a {@link Unit database unit} and can have persistent properties.
 */
@Immutable
@TODO(task = "Introduce a superinterface ReferenceableObject without the methods of this interface?", date = "2017-04-15", author = Author.KASPAR_ETTER)
public interface Subject<@Unspecifiable UNIT extends Unit> extends RootInterface {
    
    /* -------------------------------------------------- Unit -------------------------------------------------- */
    
    /**
     * Returns the unit to which this subject belongs.
     */
    @Pure
    @Provided
    @Default("net.digitalid.utility.storage.interfaces.Unit.DEFAULT")
    public @Nonnull UNIT getUnit();
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    /**
     * Generates and returns the {@link SubjectModule} required to store persistent properties.
     */
    @Pure
    @GenerateSubjectModule
    @TODO(task = "Make it possible that this method can be called getModule() without generating a corresponding field.", date = "2016-12-11", author = Author.KASPAR_ETTER)
    public @Nullable SubjectModule<UNIT, ?> module();
    
}
