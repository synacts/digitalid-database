package net.digitalid.utility.database.declaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.annotations.state.Pure;
import net.digitalid.utility.annotations.state.Validated;
import net.digitalid.utility.collections.annotations.freezable.NonFrozen;
import net.digitalid.utility.collections.freezable.FreezableArray;
import net.digitalid.utility.collections.index.MutableIndex;

/**
 * This class implements a non-unique version of another declaration.
 */
@Immutable
public final class NonUniqueDeclaration extends ChainingDeclaration {
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new non-unique declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     */
    private NonUniqueDeclaration(@Nonnull Declaration declaration) {
        super(declaration);
    }
    
    /**
     * Returns a new non-unique declaration with the given declaration.
     * 
     * @param declaration declaration on which the new declaration is based.
     * 
     * @return a new non-unique declaration with the given declaration.
     */
    @Pure
    public static @Nonnull NonUniqueDeclaration get(@Nonnull Declaration declaration) {
        return new NonUniqueDeclaration(declaration);
    }
    
    /* -------------------------------------------------- Columns -------------------------------------------------- */
    
    @Pure
    @Override
    protected int getNumberOfColumns(boolean unique) {
        return unique ? 0 : getDeclaration().getNumberOfColumns(false);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    @Override
    protected void storeColumnNames(boolean unique, @Nullable @Validated String alias, @Nullable @Validated String prefix, @NonCapturable @Nonnull @NonFrozen FreezableArray<String> names, @Nonnull MutableIndex index) {
        if (!unique) { getDeclaration().storeColumnNames(unique, alias, prefix, names, index); }
    }
    
}
