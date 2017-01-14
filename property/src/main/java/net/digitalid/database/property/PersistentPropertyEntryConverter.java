package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.value.PersistentValuePropertyEntryConverter;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.site.Site;

/**
 * This class converts the {@link PersistentPropertyEntry entries} of the {@link PersistentPropertyTable property table}.
 * 
 * @see PersistentValuePropertyEntryConverter
 */
@Immutable
public abstract class PersistentPropertyEntryConverter<SITE extends Site<?>, SUBJECT extends Subject<SITE>, ENTRY extends PersistentPropertyEntry<SUBJECT>> implements Converter<ENTRY, @Nonnull SITE> {
    
    /* -------------------------------------------------- Property Table -------------------------------------------------- */
    
    /**
     * Returns the property table to which this entry converter belongs.
     */
    @Pure
    public abstract @Nonnull PersistentPropertyTable<SITE, SUBJECT, ENTRY> getPropertyTable();
    
    /* -------------------------------------------------- Name -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getTypeName() {
        return getPropertyTable().getFullNameWithUnderlines();
    }
    
}
