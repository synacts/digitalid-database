package net.digitalid.database.testing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.validation.annotations.string.CodeIdentifier;

import net.digitalid.database.storage.Site;

/**
 *
 */
public class TestHost extends Site {
    
    @Pure
    @Override
    public boolean isHost() {
        return true;
    }
    
    public static final @Nonnull String SCHEMA_NAME = "test_host";
    
    @Pure
    @Override
    public @Nonnull @MaxSize(64) @CodeIdentifier String getDatabaseName() {
        return SCHEMA_NAME;
    }
    
    @Pure
    public @Nonnull String toString() {
        return getDatabaseName();
    }
    
    public TestHost() {
    }
    
    // TODO: refactor in the future. References, if needed, should be included using the SQL AST.
    @Pure
    @Override
    public @Nonnull String getEntityReference() {
        return "REFERENCES general_identity (identity) ON DELETE RESTRICT ON UPDATE RESTRICT";
    }
    
    @Pure
    @Override
    public boolean equals(Object object) {
        throw new UnsupportedOperationException("equals in TestHost is not supported yet.");
    }
    
    @Pure
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode in TestHost is not supported yet.");
    }
    
}
