package net.digitalid.database.testing.h2;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.freezable.FreezableHashMap;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
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
    
    private final @Nonnull @NonNullableElements FreezableHashMap<Class<?>, Transcriber<?>> transcribers = FreezableHashMap.get();
    
    public H2Dialect() {
        transcribers.put(SQLIdentifier.class, new H2SQLIdentifierTranscriber<>(SQLIdentifier.class));
        transcribers.put(SQLQualifiedTableName.class, new H2SQLIdentifierTranscriber<>(SQLQualifiedTableName.class));
        transcribers.put(SQLQualifiedColumnName.class, new H2SQLIdentifierTranscriber<>(SQLQualifiedColumnName.class));
        transcribers.put(SQLColumnName.class, new H2SQLIdentifierTranscriber<>(SQLColumnName.class));
        transcribers.put(SQLAlias.class, new H2SQLIdentifierTranscriber<>(SQLAlias.class));
        transcribers.put(SQLNumberReference.class, new H2SQLIdentifierTranscriber<>(SQLNumberReference.class));
    }
    
    @Override
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLNode<?> node, boolean parameterizable) throws InternalException {
        if (transcribers.containsKey(node.getClass())) {
            transcribers.get(node.getClass()).transcribeNode(this, node, site, string, parameterizable);
        } else {
            super.transcribe(site, string, node, parameterizable);
        }
    }
    
}
