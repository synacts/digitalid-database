package net.digitalid.database.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Unspecifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.storage.Table;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.property.value.PersistentValuePropertyTable;
import net.digitalid.database.subject.Subject;
import net.digitalid.database.subject.SubjectModule;

/**
 * A property table belongs to a {@link SubjectModule subject module} and stores the {@link PersistentPropertyEntry property entries}.
 * 
 * @see PersistentValuePropertyTable
 */
@Immutable
public interface PersistentPropertyTable<@Unspecifiable UNIT extends Unit, @Unspecifiable SUBJECT extends Subject<UNIT>, @Unspecifiable ENTRY extends PersistentPropertyEntry<SUBJECT>> extends Table<ENTRY, @Nonnull UNIT> {
    
    /* -------------------------------------------------- Parent Module -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull SubjectModule<UNIT, SUBJECT> getParentModule();
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    @Pure
    @Override
    public @Nonnull PersistentPropertyEntryConverter<UNIT, SUBJECT, ENTRY> getEntryConverter();
    
}
