package net.digitalid.database.testing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;

import net.digitalid.database.core.Site;

public class TestHost implements Site {
    
    public static final @Nonnull String SCHEMA_NAME = "test_host";
    
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
