package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.database.core.table.Site;

/**
 *
 */
public class TestHost extends Site {
    
    public TestHost() {
        super("test_host");
    }
    
    // TODO: refactor in the future. References, if needed, should be included using the SQL AST.
    @Override
    public @Nonnull String getEntityReference() {
        return "REFERENCES general_identity (identity) ON DELETE RESTRICT ON UPDATE RESTRICT";
    }
    
}
