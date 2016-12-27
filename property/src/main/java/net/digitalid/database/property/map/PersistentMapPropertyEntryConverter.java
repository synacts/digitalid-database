package net.digitalid.database.property.map;

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
import net.digitalid.utility.conversion.converter.Decoder;
import net.digitalid.utility.conversion.converter.Encoder;
import net.digitalid.utility.conversion.converter.Representation;
import net.digitalid.utility.conversion.converter.types.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.validation.annotations.string.DomainName;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.annotations.type.Embedded;
import net.digitalid.database.property.PersistentPropertyEntryConverter;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.site.Site;

/**
 * This class converts the {@link PersistentMapPropertyEntry entries} of the {@link PersistentMapPropertyTable map property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentMapPropertyEntryConverter<SITE extends Site<?>, SUBJECT extends Subject<SITE>, KEY, VALUE, PROVIDED_FOR_KEY, PROVIDED_FOR_VALUE> extends PersistentPropertyEntryConverter<SITE, SUBJECT, PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PersistentMapPropertyTable<SITE, SUBJECT, KEY, VALUE, PROVIDED_FOR_KEY, PROVIDED_FOR_VALUE> getPropertyTable();
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Class<? super PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>> getType() {
        return PersistentMapPropertyEntry.class;
    }
    
    /* -------------------------------------------------- Package -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @DomainName String getTypePackage() {
        return "net.digitalid.database.property.map";
    }
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    @Override
    @TODO(task = "Support @Cached on methods without parameters.", date = "2016-09-24", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.LOW)
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields(@Nonnull Representation representation) {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getParentModule().getSubjectConverter()), getPropertyTable().getParentModule().getSubjectConverter().getTypeName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class))),
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getKeyConverter()), "key", ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class)/* TODO: Pass them? Probably pass the whole custom field instead. */)),
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getValueConverter()), "value", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class), CustomAnnotation.with(Embedded.class)/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <X extends ExternalException> int convert(@Nullable @NonCaptured @Unmodified PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> entry, @Nonnull @NonCaptured @Modified Encoder<X> encoder) throws X {
        int i = 1;
        i *= getPropertyTable().getParentModule().getSubjectConverter().convert(entry == null ? null : entry.getSubject(), encoder);
        i *= getPropertyTable().getKeyConverter().convert(entry == null ? null : entry.getKey(), encoder);
        i *= getPropertyTable().getValueConverter().convert(entry == null ? null : entry.getValue(), encoder);
        return i;
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    @Review(comment = "How would you handle the nullable recovered objects?", date = "2016-09-30", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.LOW)
    public @Capturable <X extends ExternalException> @Nullable PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> recover(@Nonnull @NonCaptured @Modified Decoder<X> decoder, @Nonnull SITE site) throws X {
        final @Nullable SUBJECT subject = getPropertyTable().getParentModule().getSubjectConverter().recover(decoder, site);
        if (subject == null) { return null; }
        final @Nullable KEY key = getPropertyTable().getKeyConverter().recover(decoder, getPropertyTable().getProvidedObjectForKeyExtractor().evaluate(subject));
        if (key == null) { return null; }
        final @Nullable VALUE value = getPropertyTable().getValueConverter().recover(decoder, getPropertyTable().getProvidedObjectForValueExtractor().evaluate(subject, key));
        if (value == null) { return null; }
        return new PersistentMapPropertyEntrySubclass<>(subject, key, value);
    }
    
}
