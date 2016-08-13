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
import net.digitalid.utility.immutable.ImmutableMap;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeConverter;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.metadata.PrimaryKey;
import net.digitalid.database.property.PropertyEntryConverter;

/**
 * Description.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SimplePropertyEntryConverter<O, V> extends PropertyEntryConverter<O, V, SimplePropertyEntry<O, V>> {
    
    @Pure
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields() {
        return ImmutableList.withElements(CustomField.with(CustomType.TUPLE.of(getObjectConverter()), "object" /* TODO: Use the name of the subject's type instead. */, ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class, ImmutableMap.withNoEntries()), CustomAnnotation.with(Nonnull.class, ImmutableMap.withNoEntries()))),
                CustomField.with(CustomType.TUPLE.of(TimeConverter.INSTANCE), "time", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class, ImmutableMap.withNoEntries()))),
                CustomField.with(CustomType.TUPLE.of(getValueConverter()), "value", ImmutableList.withElements(/* TODO: Pass them? Probably pass the whole custom field instead. */)));
    }
    
    @Pure
    @Override
    public int convert(@Nullable @NonCaptured @Unmodified SimplePropertyEntry<O, V> object, @Nonnull @NonCaptured @Modified ValueCollector valueCollector) {
        int i = 1;
        i *= getObjectConverter().convert(object == null ? null : object.getObject(), valueCollector);
        i *= TimeConverter.INSTANCE.convert(object == null ? null : object.getTime(), valueCollector);
        i *= getValueConverter().convert(object == null ? null : object.getValue(), valueCollector);
        return i;
    }
    
    @Pure
    @Override
    public @Nonnull @Capturable SimplePropertyEntry<O, V> recover(@Nonnull @NonCaptured @Modified SelectionResult selectionResult, @Nullable Object externallyProvided) {
        final @Nonnull O object = getObjectConverter().recover(selectionResult, externallyProvided);
        final @Nonnull Time time = TimeConverter.INSTANCE.recover(selectionResult, null);
        final @Nonnull V value = getValueConverter().recover(selectionResult, externallyProvided); // TODO: @Nullable?
        return new SimplePropertyEntrySubclass<>(object, time, value);
    }
    
}
