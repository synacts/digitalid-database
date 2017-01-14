package net.digitalid.database.subject.site;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.conversion.enumerations.Representation;
import net.digitalid.utility.conversion.exceptions.ConnectionException;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.conversion.interfaces.Decoder;
import net.digitalid.utility.conversion.interfaces.Encoder;
import net.digitalid.utility.conversion.model.CustomField;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.string.Strings;
import net.digitalid.utility.validation.annotations.generation.Provided;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.string.DomainName;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class converts a specific {@link Site site} so that the site itself is {@link Provided provided}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SiteConverter<SITE extends Site<?>> implements Converter<SITE, SITE> {
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getTypeName() {
        return getType().getSimpleName();
    }
    
    @Pure
    @Override
    public @Nonnull @DomainName String getTypePackage() {
        return Strings.substringUntilLast(getType().getCanonicalName(), '.');
    }
    
    private static final @Nonnull ImmutableList<@Nonnull CustomField> FIELDS = ImmutableList.withElements();
    
    @Pure
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields(@Nonnull Representation representation) {
        return FIELDS;
    }
    
    @Pure
    @Override
    public <X extends ConnectionException> int convert(@NonCaptured @Unmodified @Nullable SITE site, @NonCaptured @Modified @Nonnull Encoder<X> encoder) throws X {
        return 1;
    }
    
    @Pure
    @Override
    public @Capturable <X extends ConnectionException> @Nullable SITE recover(@NonCaptured @Modified @Nonnull Decoder<X> decoder, @Nonnull SITE site) throws X {
        return site;
    }
    
}
