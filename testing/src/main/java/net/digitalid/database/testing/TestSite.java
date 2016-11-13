package net.digitalid.database.testing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.interfaces.Site;

@Immutable
public class TestSite implements Site {
    
    public static final @Nonnull TestSite INSTANCE = new TestSite();
    
    public static final @Nonnull String SCHEMA_NAME = "default";
    
    @Pure
    @Override
    public @Nonnull @CodeIdentifier @MaxSize(63) String getName() {
        return SCHEMA_NAME;
    }
    
    @Pure
    @Override
    public @Nonnull String toString() {
        return getName();
    }
    
}
