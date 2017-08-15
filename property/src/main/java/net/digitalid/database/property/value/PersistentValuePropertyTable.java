package net.digitalid.database.property.value;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.exceptions.ConnectionException;
import net.digitalid.utility.conversion.exceptions.RecoveryException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomAnnotation;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.interceptors.Cached;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.TableImplementation;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeConverter;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.string.DomainName;
import net.digitalid.utility.validation.annotations.type.Immutable;
import net.digitalid.utility.validation.annotations.value.Valid;

import net.digitalid.database.annotations.constraints.PrimaryKey;
import net.digitalid.database.property.PersistentPropertyTable;
import net.digitalid.database.property.subject.Subject;

/**
 * The persistent value property table stores the {@link PersistentValuePropertyEntry value property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentValuePropertyTable<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Specifiable VALUE, @Specifiable PROVIDED_FOR_VALUE> extends TableImplementation<PersistentValuePropertyEntry<SUBJECT, VALUE>, UNIT> implements PersistentPropertyTable<UNIT, SUBJECT, PersistentValuePropertyEntry<SUBJECT, VALUE>>, Valid.Value<VALUE> {
    
    /* -------------------------------------------------- Provided Object Extractor -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object from the subject.
     */
    @Pure
    @Default("subject -> null")
    public abstract @Nonnull UnaryFunction<@Nonnull SUBJECT, PROVIDED_FOR_VALUE> getProvidedObjectExtractor();
    
    /* -------------------------------------------------- Value Converter -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the value of the property.
     */
    @Pure
    public abstract @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> getValueConverter();
    
    /* -------------------------------------------------- Default Value -------------------------------------------------- */
    
    /**
     * Returns the default value of the property.
     */
    @Pure
    public abstract @Valid VALUE getDefaultValue();
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Class<? super PersistentValuePropertyEntry<SUBJECT, VALUE>> getType() {
        return PersistentValuePropertyEntry.class;
    }
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getTypeName() {
        return "PersistentValuePropertyEntry";
    }
    
    /* -------------------------------------------------- Package -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @DomainName String getTypePackage() {
        return "net.digitalid.database.property.value";
    }
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    @Cached
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields(@Nonnull Representation representation) {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getParentModule().getSubjectTable()), getParentModule().getSubjectTable().getTypeName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class))),
                CustomField.with(CustomType.TUPLE.of(TimeConverter.INSTANCE), "time", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class))),
                CustomField.with(CustomType.TUPLE.of(getValueConverter()), "value", ImmutableList.withElements(/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <@Unspecifiable EXCEPTION extends ConnectionException> void convert(@Nonnull @NonCaptured @Unmodified PersistentValuePropertyEntry<SUBJECT, VALUE> entry, @Nonnull @NonCaptured @Modified Encoder<EXCEPTION> encoder) throws EXCEPTION {
        getParentModule().getSubjectTable().convert(entry.getSubject(), encoder);
        TimeConverter.INSTANCE.convert(entry.getTime(), encoder);
        getValueConverter().convert(entry.getValue(), encoder);
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    public @Capturable <@Unspecifiable EXCEPTION extends ConnectionException> @Nonnull PersistentValuePropertyEntry<SUBJECT, VALUE> recover(@Nonnull @NonCaptured @Modified Decoder<EXCEPTION> decoder, @Nonnull UNIT unit) throws EXCEPTION, RecoveryException {
        final @Nonnull SUBJECT subject = getParentModule().getSubjectTable().recover(decoder, unit);
        final @Nonnull Time time = TimeConverter.INSTANCE.recover(decoder, null);
        final VALUE value = getValueConverter().recover(decoder, getProvidedObjectExtractor().evaluate(subject));
        return new PersistentValuePropertyEntrySubclass<>(subject, time, value);
    }
    
}
