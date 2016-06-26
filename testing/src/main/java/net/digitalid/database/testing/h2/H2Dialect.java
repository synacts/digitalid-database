package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.map.FreezableHashMap;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.number.SQLNumberReference;
import net.digitalid.database.dialect.ast.identifier.SQLAlias;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLIdentifier;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 *
 */
public class H2Dialect extends SQLDialect {
    
    private final @Nonnull FreezableHashMap<@Nonnull Class<?>, @Nonnull Transcriber<?>> transcribers = FreezableHashMap.withDefaultCapacity();
    
    public H2Dialect() {
        transcribers.put(SQLIdentifier.class, new H2SQLIdentifierTranscriber<>(SQLIdentifier.class));
        transcribers.put(SQLQualifiedTableName.class, new H2SQLIdentifierTranscriber<>(SQLQualifiedTableName.class));
        transcribers.put(SQLQualifiedColumnName.class, new H2SQLIdentifierTranscriber<>(SQLQualifiedColumnName.class));
        transcribers.put(SQLColumnName.class, new H2SQLIdentifierTranscriber<>(SQLColumnName.class));
        transcribers.put(SQLAlias.class, new H2SQLIdentifierTranscriber<>(SQLAlias.class));
        transcribers.put(SQLNumberReference.class, new H2SQLIdentifierTranscriber<>(SQLNumberReference.class));
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
