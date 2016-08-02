package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;

/**
 * This class implements a database table that can be created and deleted.
 */
@Mutable
@GenerateBuilder
@GenerateSubclass
public abstract class Table extends RootClass implements Storage {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    @Pure
    @Override
    @CallSuper
    protected void initialize() {
        final @Nullable Module module = getModule();
        if (module != null) { module.addSubstorage(this); }
        super.initialize();
    }
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Returns the converters that declare the columns of this table.
     */
    @Pure
    public abstract @Nonnull ImmutableList<@Nonnull Converter<?, ?>> getConverters();
    
    /* -------------------------------------------------- Tables -------------------------------------------------- */
    
    @Impure
    @Override
    @NonCommitting
    public void createTables(@Nonnull Site site) throws DatabaseException {
        // TODO: Implement!
    }
    
    @Impure
    @Override
    @NonCommitting
    public void deleteTables(@Nonnull Site site) throws DatabaseException {
        // TODO: Implement!
    }
    
}
