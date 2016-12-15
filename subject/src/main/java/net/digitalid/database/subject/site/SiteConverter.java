package net.digitalid.database.subject.site;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.annotations.parameter.Unmodified;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.conversion.converter.SelectionResult;
import net.digitalid.utility.conversion.converter.ValueCollector;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.logging.exceptions.ExternalException;
import net.digitalid.utility.validation.annotations.generation.Provided;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class converts a specific {@link Site site} so that the site itself is {@link Provided provided}.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class SiteConverter<SITE extends Site<SITE>> implements Converter<SITE, SITE> {
    
    /**
     * Returns the class object of the site that is converted.
     */
    @Pure
    public abstract @Nonnull Class<SITE> getSiteClass();
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName() {
        return getSiteClass().getSimpleName();
    }
    
    private static final @Nonnull ImmutableList<@Nonnull CustomField> FIELDS = ImmutableList.withElements();
    
    @Pure
    @Override
    public @Nonnull ImmutableList<@Nonnull CustomField> getFields() {
        return FIELDS;
    }
    
    @Pure
    @Override
    public <X extends ExternalException> int convert(@NonCaptured @Unmodified @Nullable SITE site, @NonCaptured @Modified @Nonnull ValueCollector<X> valueCollector) throws X {
        return 1;
    }
    
    @Pure
    @Override
    public @Capturable <X extends ExternalException> @Nullable SITE recover(@NonCaptured @Modified @Nonnull SelectionResult<X> selectionResult, @Nonnull SITE site) throws X {
        return site;
    }
    
}
