package net.digitalid.database.dialect.ast.identifier;

import javax.annotation.Nonnull;

import net.digitalid.utility.validation.annotations.size.MaxSize;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.Transcriber;

/**
 * This class represents an SQL identifier.
 * The subclasses increase the type safety.
 * 
 * @see SQLName
 * @see SQLAlias
 * @see SQLPrefix
 */
@Immutable
public interface SQLIdentifier<T> extends SQLNode<T> {
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    
    
    @Nonnull @MaxSize(63) String getValue();    
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
     /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    
    final class SQLIdentifierTranscriber<T extends SQLIdentifier<T>> extends Transcriber<T> {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull T node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            string.append("\"").append(node.getValue()).append("\"");
        }
        
    };
    
}
