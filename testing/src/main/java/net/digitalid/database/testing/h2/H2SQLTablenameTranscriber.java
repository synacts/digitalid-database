package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.storage.Site;

/**
 *
 */
public class H2SQLTablenameTranscriber extends Transcriber<SQLQualifiedTableName> {
    
    H2SQLTablenameTranscriber() {
        super(SQLQualifiedTableName.class);
    }
    
    @Pure
    @Override
    protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLQualifiedTableName node, @Nonnull Site site) throws InternalException {
        return (site.getDatabaseName().isEmpty() ? "" : site.getDatabaseName() + ".") + node.tableName.toUpperCase();
    }
    
}
