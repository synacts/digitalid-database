package net.digitalid.database.property.set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.collaboration.annotations.Review;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;
import net.digitalid.utility.conversion.converter.CustomAnnotation;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.conversion.converter.SelectionResult;
import net.digitalid.utility.conversion.converter.ValueCollector;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.type.Embedded;
import net.digitalid.database.property.PersistentPropertyEntryConverter;
import net.digitalid.database.subject.site.Site;
import net.digitalid.database.subject.Subject;

/**
 * This class converts the {@link PersistentSetPropertyEntry entries} of the {@link PersistentSetPropertyTable set property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentSetPropertyEntryConverter<S extends Subject, V, E> extends PersistentPropertyEntryConverter<S, PersistentSetPropertyEntry<S, V>> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PersistentSetPropertyTable<S, V, E> getPropertyTable();
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    @Override
    @TODO(task = "Support @Cached on methods without parameters.", date = "2016-09-24", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.LOW)
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields() {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getParentModule().getSubjectConverter()), getPropertyTable().getParentModule().getSubjectConverter().getName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class))),
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getValueConverter()), "value", ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class)/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <X extends ExternalException> int convert(@Nullable @NonCaptured @Unmodified PersistentSetPropertyEntry<S, V> entry, @Nonnull @NonCaptured @Modified ValueCollector<X> valueCollector) throws X {
        int i = 1;
        i *= getPropertyTable().getParentModule().getSubjectConverter().convert(entry == null ? null : entry.getSubject(), valueCollector);
        i *= getPropertyTable().getValueConverter().convert(entry == null ? null : entry.getValue(), valueCollector);
        return i;
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    @Review(comment = "How would you handle the nullable recovered objects?", date = "2016-09-30", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.LOW)
    public @Capturable <X extends ExternalException> @Nullable PersistentSetPropertyEntry<S, V> recover(@Nonnull @NonCaptured @Modified SelectionResult<X> selectionResult, @Nonnull Site site) throws X {
        final @Nullable S subject = getPropertyTable().getParentModule().getSubjectConverter().recover(selectionResult, site);
        if (subject != null) {
            final @Nullable V value = getPropertyTable().getValueConverter().recover(selectionResult, getPropertyTable().getProvidedObjectExtractor().evaluate(subject));
            return value != null ? new PersistentSetPropertyEntrySubclass<>(subject, value) : null;
        } else {
            return null;
        }
    }
    
}
