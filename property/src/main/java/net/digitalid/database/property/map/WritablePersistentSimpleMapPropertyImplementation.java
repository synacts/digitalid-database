package net.digitalid.database.property.map;

import javax.annotation.Nonnull;

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
import net.digitalid.database.subject.site.Site;

/**
 * This class implements the {@link WritablePersistentSimpleMapProperty}.
 */
@ThreadSafe
@GenerateBuilder
@GenerateSubclass
public abstract class WritablePersistentSimpleMapPropertyImplementation<SITE extends Site<?>, SUBJECT extends Subject<SITE>, KEY, VALUE> extends WritablePersistentMapPropertyImplementation<SITE, SUBJECT, KEY, VALUE, ReadOnlyMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>, FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE>> implements WritablePersistentSimpleMapProperty<SUBJECT, KEY, VALUE> {
    
    @Pure
    @Override
    @Default("net.digitalid.utility.collections.map.FreezableLinkedHashMapBuilder.build()")
    protected abstract @Nonnull @NonFrozen FreezableMap<@Nonnull @Valid("key") KEY, @Nonnull @Valid VALUE> getMap();
    
}