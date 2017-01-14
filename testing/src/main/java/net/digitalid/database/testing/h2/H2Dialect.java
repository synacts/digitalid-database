package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.collections.map.FreezableHashMapBuilder;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.dialect.expression.number.SQLNumberReference;
import net.digitalid.database.dialect.identifier.SQLAlias;
import net.digitalid.database.dialect.identifier.SQLBooleanAlias;
import net.digitalid.database.dialect.identifier.SQLColumnName;
import net.digitalid.database.dialect.identifier.SQLIdentifier;
import net.digitalid.database.dialect.identifier.SQLQualifiedColumn;
import net.digitalid.database.dialect.identifier.table.SQLQualifiedTable;
import net.digitalid.database.subject.site.Site;

public class H2Dialect extends SQLDialect {
    
    private final @Nonnull FreezableHashMap<@Nonnull Class<?>, @Nonnull Transcriber<?>> transcribers = FreezableHashMapBuilder.build();
    
    public H2Dialect() {
        transcribers.put(SQLIdentifier.class, new H2SQLIdentifierTranscriber<>(SQLIdentifier.class));
        transcribers.put(SQLQualifiedColumn.class, new H2SQLIdentifierTranscriber<>(SQLQualifiedColumn.class));
        transcribers.put(SQLColumnName.class, new H2SQLIdentifierTranscriber<>(SQLColumnName.class));
        transcribers.put(SQLAlias.class, new H2SQLIdentifierTranscriber<>(SQLAlias.class));
        transcribers.put(SQLBooleanAlias.class, new H2SQLIdentifierTranscriber<>(SQLBooleanAlias.class));
        transcribers.put(SQLNumberReference.class, new H2SQLIdentifierTranscriber<>(SQLNumberReference.class));
    
        transcribers.put(SQLQualifiedTable.class, new H2SQLTablenameTranscriber());
    }
    
    @Pure
    @Override
    public @Nonnull String transcribe(@Nonnull Site site, @Nonnull SQLNode<?> node) throws InternalException {
        if (transcribers.containsKey(node.getClass())) {
            return transcribers.get(node.getClass()).transcribeNode(this, node, site);
        } else {
            return super.transcribe(site, node);
        }
    }
    
}
