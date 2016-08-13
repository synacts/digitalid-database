package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.storage.Table;

/**
 * Description.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class PropertyTable<O, V> extends Table {
    
    @Pure
    @Override
    public abstract @Nonnull ObjectModule<O> getParentModule();
    
    @Pure
    @Override
    public abstract @Nonnull PropertyEntryConverter<O, V, ?> getConverter();
    
    /**
     * Returns the validator which validates the encapsulated value(s).
     */
    @Pure
    @Default("object -> true")
    public abstract @Nonnull Predicate<? super V> getValueValidator();
    
}
