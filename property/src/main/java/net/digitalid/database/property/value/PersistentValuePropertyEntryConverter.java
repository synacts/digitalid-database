package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.collaboration.enumerations.Priority;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.exceptions.ConnectionException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeConverter;
import net.digitalid.utility.validation.annotations.string.DomainName;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.PersistentPropertyEntryConverter;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.unit.Unit;

/**
 * This class converts the {@link PersistentValuePropertyEntry entries} of the {@link PersistentValuePropertyTable value property table}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentValuePropertyEntryConverter<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Specifiable VALUE, @Specifiable PROVIDED_FOR_VALUE> extends PersistentPropertyEntryConverter<UNIT, SUBJECT, PersistentValuePropertyEntry<SUBJECT, VALUE>> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    @Pure
    @Override
    public abstract @Nonnull PersistentValuePropertyTable<UNIT, SUBJECT, VALUE, PROVIDED_FOR_VALUE> getPropertyTable();
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Class<? super PersistentValuePropertyEntry<SUBJECT, VALUE>> getType() {
        return PersistentValuePropertyEntry.class;
    }
    
    /* -------------------------------------------------- Package -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @DomainName String getTypePackage() {
        return "net.digitalid.database.property.value";
    }
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    @Override
    @TODO(task = "Support @Cached on methods without parameters.", date = "2016-09-24", author = Author.KASPAR_ETTER, assignee = Author.STEPHANIE_STROKA, priority = Priority.LOW)
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields(@Nonnull Representation representation) {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getParentModule().getSubjectConverter()), getPropertyTable().getParentModule().getSubjectConverter().getTypeName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class))),
                CustomField.with(CustomType.TUPLE.of(TimeConverter.INSTANCE), "time", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class))),
                CustomField.with(CustomType.TUPLE.of(getPropertyTable().getValueConverter()), "value", ImmutableList.withElements(/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <@Unspecifiable EXCEPTION extends ConnectionException> void convert(@Nonnull @NonCaptured @Unmodified PersistentValuePropertyEntry<SUBJECT, VALUE> entry, @Nonnull @NonCaptured @Modified Encoder<EXCEPTION> encoder) throws EXCEPTION {
        getPropertyTable().getParentModule().getSubjectConverter().convert(entry.getSubject(), encoder);
        TimeConverter.INSTANCE.convert(entry.getTime(), encoder);
        getPropertyTable().getValueConverter().convert(entry.getValue(), encoder);
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    public @Capturable <@Unspecifiable EXCEPTION extends ConnectionException> @Nonnull PersistentValuePropertyEntry<SUBJECT, VALUE> recover(@Nonnull @NonCaptured @Modified Decoder<EXCEPTION> decoder, @Nonnull UNIT unit) throws EXCEPTION, RecoveryException {
        final @Nonnull SUBJECT subject = getPropertyTable().getParentModule().getSubjectConverter().recover(decoder, unit);
        final @Nonnull Time time = TimeConverter.INSTANCE.recover(decoder, null);
        final VALUE value = getPropertyTable().getValueConverter().recover(decoder, getPropertyTable().getProvidedObjectExtractor().evaluate(subject));
        return new PersistentValuePropertyEntrySubclass<>(subject, time, value);
    }
    
}
