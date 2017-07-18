package net.digitalid.database.subject;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.Module;
import net.digitalid.utility.storage.Table;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * A subject module contains the tables of all properties in the subject's class.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SubjectModule<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>> extends Module {
    
    /* -------------------------------------------------- Subject Table -------------------------------------------------- */
    
    /**
     * Returns the converter used to convert and recover the subject.
     */
    @Pure
    public abstract @Nonnull Table<SUBJECT, @Nonnull UNIT> getSubjectTable();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName() {
        return getSubjectTable().getTypeName();
    }
    
}
