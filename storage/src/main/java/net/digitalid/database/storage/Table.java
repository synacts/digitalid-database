package net.digitalid.database.storage;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This type models a database table that is described by the given converter.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface Table<ENTRY, PROVIDED> extends Storage {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    /**
     * Returns the converter that models the columns of this table and converts its entries.
     */
    @Pure
    public @Nonnull Converter<ENTRY, PROVIDED> getEntryConverter();
    
}
