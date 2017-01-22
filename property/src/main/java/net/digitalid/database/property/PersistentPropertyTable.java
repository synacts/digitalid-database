package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.value.PersistentValuePropertyTable;
import net.digitalid.database.storage.Table;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.SubjectModule;
import net.digitalid.database.unit.Unit;

/**
 * A property table belongs to a {@link SubjectModule subject module} and stores the {@link PersistentPropertyEntry property entries}.
 * 
 * @see PersistentValuePropertyTable
 */
@Immutable
public interface PersistentPropertyTable<SITE extends Unit<?>, SUBJECT extends Subject<SITE>, ENTRY extends PersistentPropertyEntry<SUBJECT>> extends Table<ENTRY, @Nonnull SITE> {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SubjectModule<SITE, SUBJECT> getParentModule();
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentPropertyEntryConverter<SITE, SUBJECT, ENTRY> getEntryConverter();
    
}
