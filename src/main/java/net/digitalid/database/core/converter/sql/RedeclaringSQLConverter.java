package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import net.digitalid.database.core.converter.key.NonConvertingKeyConverter;
import net.digitalid.database.core.declaration.Declaration;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Matching;
import net.digitalid.utility.annotations.state.Pure;

/**
 * This class implements an SQL converter that redeclares another SQL converter.
 * 
 * @param <O> the type of the objects that this converter can store and restore (typically the surrounding class).
 * @param <E> the type of the external object that is needed to restore an object, which is quite often an entity.
 *            In case no external information is needed for the restoration of an object, declare it as an object.
 * 
 * @see NonConvertingKeyConverter
 */
@Immutable
public final class RedeclaringSQLConverter<O, E> extends ChainingSQLConverter<O, E, O, E> {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new redeclaring SQL converter with the given parameters.
     * 
     * @param declaration the declaration that replaces the existing declaration.
     * @param SQLConverter the SQL converter used to store and restore the objects.
     */
    private RedeclaringSQLConverter(@Nonnull @Matching Declaration declaration, @Nonnull SQLConverter<O, E> SQLConverter) {
        super(declaration, NonConvertingKeyConverter.<O, E>get(), SQLConverter);
    }
    
    /**
     * Returns a new redeclaring SQL converter with the given parameters.
     * 
     * @param declaration the declaration that replaces the existing declaration.
     * @param SQLConverter the SQL converter used to store and restore the objects.
     * 
     * @return a new redeclaring SQL converter with the given parameters.
     */
    @Pure
    public static @Nonnull <O, E> RedeclaringSQLConverter<O, E> get(@Nonnull @Matching Declaration declaration, @Nonnull SQLConverter<O, E> SQLConverter) {
        return new RedeclaringSQLConverter<>(declaration, SQLConverter);
    }
    
}
