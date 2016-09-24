package net.digitalid.database.storage;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * This class models a database table that is described by the given converter.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class Table<E> extends Storage {
    
    /* -------------------------------------------------- Entry Converter -------------------------------------------------- */
    
    /**
     * Returns the converter that models the columns of this table and converts its entries.
     */
    @Pure
    public abstract @Nonnull Converter<E, Void> getEntryConverter();
    
}
