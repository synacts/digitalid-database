/*
 * Copyright (C) 2017 Synacts GmbH, Switzerland (info@synacts.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.digitalid.database.property.map;

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
import net.digitalid.utility.functional.interfaces.BinaryFunction;
import net.digitalid.utility.functional.interfaces.UnaryFunction;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.interceptors.Cached;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.TableImplementation;
import net.digitalid.utility.storage.interfaces.Unit;
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
 * The map property table stores the {@link PersistentMapPropertyEntry map property entries}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class PersistentMapPropertyTable<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable KEY, @Unspecifiable VALUE, @Specifiable PROVIDED_FOR_KEY, @Specifiable PROVIDED_FOR_VALUE> extends TableImplementation<PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>, UNIT> implements PersistentPropertyTable<UNIT, SUBJECT, PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>>, Valid.Key<KEY>, Valid.Value<VALUE> {
    
    /* -------------------------------------------------- Extractors -------------------------------------------------- */
    
    /**
     * Returns the function that extracts the externally provided object for the key from the subject.
     */
    @Pure
    @Default("subject -> null")
    public abstract @Nonnull UnaryFunction<@Nonnull SUBJECT, PROVIDED_FOR_KEY> getProvidedObjectForKeyExtractor();
    
    /**
     * Returns the function that extracts the externally provided object for the value from the subject and the key.
     */
    @Pure
    @Default("(subject, key) -> null")
    public abstract @Nonnull BinaryFunction<@Nonnull SUBJECT, @Nonnull KEY, PROVIDED_FOR_VALUE> getProvidedObjectForValueExtractor();
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Returns the converter to convert and recover the keys of the property.
     */
    @Pure
    public abstract @Nonnull Converter<KEY, PROVIDED_FOR_KEY> getKeyConverter();
    
    /**
     * Returns the converter to convert and recover the values of the property.
     */
    @Pure
    public abstract @Nonnull Converter<VALUE, PROVIDED_FOR_VALUE> getValueConverter();
    
    /* -------------------------------------------------- Type -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull Class<? super PersistentMapPropertyEntry<SUBJECT, KEY, VALUE>> getType() {
        return PersistentMapPropertyEntry.class;
    }
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getTypeName() {
        return "PersistentMapPropertyEntry";
    }
    
    /* -------------------------------------------------- Package -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @DomainName String getTypePackage() {
        return "net.digitalid.database.property.map";
    }
    
    /* -------------------------------------------------- Fields -------------------------------------------------- */
    
    @Pure
    @Cached
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields(@Nonnull Representation representation) {
        return ImmutableList.withElements(
                CustomField.with(CustomType.TUPLE.of(getParentModule().getSubjectTable()), getParentModule().getSubjectTable().getTypeName(), ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class))),
                CustomField.with(CustomType.TUPLE.of(getKeyConverter()), "key", ImmutableList.withElements(CustomAnnotation.with(PrimaryKey.class), CustomAnnotation.with(Nonnull.class)/* TODO: Pass them? Probably pass the whole custom field instead. */)),
                CustomField.with(CustomType.TUPLE.of(getValueConverter()), "value", ImmutableList.withElements(CustomAnnotation.with(Nonnull.class)/* TODO: Pass them? Probably pass the whole custom field instead. */))
        );
    }
    
    /* -------------------------------------------------- Convert -------------------------------------------------- */
    
    @Pure
    @Override
    public <@Unspecifiable EXCEPTION extends ConnectionException> void convert(@Nonnull @NonCaptured @Unmodified PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> entry, @Nonnull @NonCaptured @Modified Encoder<EXCEPTION> encoder) throws EXCEPTION {
        getParentModule().getSubjectTable().convert(entry.getSubject(), encoder);
        getKeyConverter().convert(entry.getKey(), encoder);
        getValueConverter().convert(entry.getValue(), encoder);
    }
    
    /* -------------------------------------------------- Recover -------------------------------------------------- */
    
    @Pure
    @Override
    public @Capturable <@Unspecifiable EXCEPTION extends ConnectionException> @Nonnull PersistentMapPropertyEntry<SUBJECT, KEY, VALUE> recover(@Nonnull @NonCaptured @Modified Decoder<EXCEPTION> decoder, @Nonnull UNIT unit) throws EXCEPTION, RecoveryException {
        final @Nonnull SUBJECT subject = getParentModule().getSubjectTable().recover(decoder, unit);
        final @Nonnull KEY key = getKeyConverter().recover(decoder, getProvidedObjectForKeyExtractor().evaluate(subject));
        final @Nonnull VALUE value = getValueConverter().recover(decoder, getProvidedObjectForValueExtractor().evaluate(subject, key));
        return new PersistentMapPropertyEntrySubclass<>(subject, key, value);
    }
    
}
