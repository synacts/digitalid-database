package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.type.ThreadSafe;
import net.digitalid.utility.collections.map.FreezableMap;
import net.digitalid.utility.collections.map.ReadOnlyMap;
import net.digitalid.utility.freezable.annotations.NonFrozen;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.subject.Subject;
import net.digitalid.database.unit.Unit;

/**
 * This class implements the {@link WritablePersistentSimpleMapProperty}.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
public abstract class WritablePersistentSimpleMapPropertyImplementation<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable KEY, @Unspecifiable VALUE> extends WritablePersistentMapPropertyImplementation<UNIT, SUBJECT, KEY, VALUE, ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>, FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> implements WritablePersistentSimpleMapProperty<SUBJECT, KEY, VALUE> {
    
    @Pure
    @Override
    @Default("net.digitalid.utility.collections.map.FreezableLinkedHashMapBuilder.build()")
    protected abstract @Nonnull @NonFrozen FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE> getMap();
    
}
