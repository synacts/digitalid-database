package net.digitalid.database.core.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.validation.state.Immutable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.validation.state.Validated;

/**
 * This class implements a nullable version of another declaration.
 */
@Immutable
public final class NullableDeclaration extends ChainingDeclaration {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new nullable declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     */
    private NullableDeclaration(@Nonnull Declaration declaration) {
        super(declaration);
    }
    
    /**
     * Returns a new nullable declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     * 
     * @return a new nullable declaration with the given declaration.
     */
    @Pure
    public static @Nonnull NullableDeclaration get(@Nonnull Declaration declaration) {
        return new NullableDeclaration(declaration);
    }
    
    /* -------------------------------------------------- Declaration -------------------------------------------------- */
    
    @Pure
    @Override
    protected @Nonnull String toString(boolean nullable, @Nullable @Validated String prefix) {
        return getDeclaration().toString(true, prefix);
    }
    
}
