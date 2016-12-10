package net.digitalid.database.subject;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class suppresses the generation of the {@link SubjectModule}, which is no problem because it has no persistent properties anyway.
 */
@Immutable
@GenerateSubclass
abstract class DefaultSite extends RootClass implements Site {
    
    /* -------------------------------------------------- Module -------------------------------------------------- */
    
    @Pure
    @Override
    public void generateModule() {}
    
    
    // TODO: Remove again!
    @Pure
    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }
    
}
