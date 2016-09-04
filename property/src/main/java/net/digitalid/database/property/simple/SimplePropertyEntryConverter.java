package net.digitalid.database.property.simple;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.conversion.converter.SelectionResult;
import net.digitalid.utility.conversion.converter.ValueCollector;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeConverter;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.metadata.Embedded;
import net.digitalid.database.annotations.metadata.PrimaryKey;
import net.digitalid.database.property.PropertyEntryConverter;

/**
 * This class converts the {@link SimplePropertyEntry entries} of the {@link SimpleObjectProperty}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SimplePropertyEntryConverter<O, V> extends PropertyEntryConverter<O, V, SimplePropertyEntry<O, V>> {
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    // TODO: Allow @Cached on methods without parameters?
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields() {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getObjectConverter()), getObjectConverter().getName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class))),
                CustomField.with(CustomType.TUPLE.of(TimeConverter.INSTANCE), "time", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class))),
                CustomField.with(CustomType.TUPLE.of(getValueConverter()), "value", ImmutableList.withElements(CustomAnnotation.with(Embedded.class)/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <X extends ExternalException> int convert(@Nullable @NonCaptured @Unmodified SimplePropertyEntry<O, V> object, @Nonnull @NonCaptured @Modified ValueCollector<X> valueCollector) throws ExternalException {
        int i = 1;
        i *= getObjectConverter().convert(object == null ? null : object.getObject(), valueCollector);
        i *= TimeConverter.INSTANCE.convert(object == null ? null : object.getTime(), valueCollector);
        i *= getValueConverter().convert(object == null ? null : object.getValue(), valueCollector);
        return i;
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    public @Capturable <X extends ExternalException> @Nonnull SimplePropertyEntry<O, V> recover(@Nonnull @NonCaptured @Modified SelectionResult<X> selectionResult, Void none) throws ExternalException {
        final @Nonnull O object = getObjectConverter().recover(selectionResult, null);
        final @Nonnull Time time = TimeConverter.INSTANCE.recover(selectionResult, null);
        final V value = getValueConverter().recover(selectionResult, null);
        return new SimplePropertyEntrySubclass<>(object, time, value);
    }
    
}
