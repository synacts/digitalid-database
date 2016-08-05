package net.digitalid.database.storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.CallSuper;
import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.type.Mutable;

import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.storage.injections.TableCreator;
import net.digitalid.database.storage.injections.TableDeleter;

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
        final @Nullable Module module = getParentModule();
        if (module != null) { module.addSubstorage(this); }
        else { Site.addStorage(this); }
        super.initialize();
    }
    
    /* -------------------------------------------------- Converters -------------------------------------------------- */
    
    /**
     * Returns the converter that models the columns of this table.
     */
    @Pure
    public abstract @Nonnull Converter<?, ?> getConverter();
    
    // TODO: Override getName() as soon as the converter has such a method by either delegatig or deriving the value.
    
    /* -------------------------------------------------- Tables -------------------------------------------------- */
    
    @Impure
    @Override
    @NonCommitting
    public void createTables(@Nonnull Site site) throws DatabaseException {
        TableCreator.create(this, site);
    }
    
    @Impure
    @Override
    @NonCommitting
    public void deleteTables(@Nonnull Site site) throws DatabaseException {
        TableDeleter.delete(this, site);
    }
    
}
