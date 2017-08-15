package net.digitalid.database.property.set;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.set.FreezableSet;
import net.digitalid.utility.collections.set.ReadOnlySet;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.property.subject.Subject;

/**
 * This class implements the {@link WritablePersistentSimpleSetProperty}.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
public abstract class WritablePersistentSimpleSetPropertyImplementation<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable VALUE> extends WritablePersistentSetPropertyImplementation<UNIT, SUBJECT, VALUE, ReadOnlySet<@Nonnull @Valid VALUE>, FreezableSet<@Nonnull @Valid VALUE>> implements WritablePersistentSimpleSetProperty<SUBJECT, VALUE> {
    
    @Pure
    @Override
    @Default("net.digitalid.utility.collections.set.FreezableLinkedHashSetBuilder.build()")
    protected abstract @Nonnull @NonFrozen FreezableSet<@Nonnull @Valid VALUE> getSet();
    
}
