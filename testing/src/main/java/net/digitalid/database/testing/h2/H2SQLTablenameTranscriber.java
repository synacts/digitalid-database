package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.subject.site.Site;

/**
 *
 */
public class H2SQLTablenameTranscriber extends Transcriber<SQLQualifiedTable> {
    
    H2SQLTablenameTranscriber() {
        super(SQLQualifiedTable.class);
    }
    
    @Pure
    @Override
    protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLQualifiedTable node, @Nonnull Site site) throws InternalException {
        return (site.getSchemaName().isEmpty() ? "" : site.getSchemaName() + ".") + node.tableName.toUpperCase();
    }
    
}
