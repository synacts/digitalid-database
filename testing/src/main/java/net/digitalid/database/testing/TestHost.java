package net.digitalid.database.testing;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;

import net.digitalid.database.core.table.Site;

/**
 *
 */
public class TestHost extends Site {
    
    public static final @Nonnull String SCHEMA_NAME = "test_host";
    
    public TestHost() {
        super(SCHEMA_NAME);
    }
    
    // TODO: refactor in the future. References, if needed, should be included using the SQL AST.
    @Pure
    @Override
    public @Nonnull String getEntityReference() {
        return "REFERENCES general_identity (identity) ON DELETE RESTRICT ON UPDATE RESTRICT";
    }
    
}
