package net.digitalid.database.core.processing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.tuples.Triplet;
import net.digitalid.utility.validation.annotations.type.Immutable;

/**
 * The data used by the parameter function to set a value into a prepared statement.
 */
@Immutable
public class ParameterFunctionData<P, T> extends Triplet<@Nonnull P, @Nonnull Integer, @Nonnull T> {
    
    /* -------------------------------------------------- Prepared Statement -------------------------------------------------- */
    
    /**
     * Returns the prepared statement that is going to be set.
     */
    @Pure
    public @Nonnull P getPreparedStatement() {
        return get0();
    }
    
    /* -------------------------------------------------- Parameter Index -------------------------------------------------- */
    
    /**
     * Returns the parameter index at which the value is going to be set into the prepared statement.
     */
    @Pure
    public @Nonnull Integer getParameterIndex() {
        return get1();
    }
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value which is set into the prepared statement.
     */
    @Pure
    public @Nonnull T getValue() {
        return get2();
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    ParameterFunctionData(@Nonnull P statement, @Nonnull Integer parameterIndex, @Nonnull T value) {
        super(statement, parameterIndex, value);
    }
    
}
