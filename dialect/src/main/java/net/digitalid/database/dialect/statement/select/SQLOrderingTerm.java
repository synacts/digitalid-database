package net.digitalid.database.dialect.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.Transcriber;
import net.digitalid.database.dialect.identifier.SQLQualifiedColumn;
import net.digitalid.database.subject.site.Site;

/**
 * This SQL node represents the ordering term of an SQL select statement.
 */
public class SQLOrderingTerm implements SQLNode<SQLOrderingTerm> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified column name of the ordering term
     */
    public final @Nonnull SQLQualifiedColumn qualifiedColumnName;
    
    /**
     * The ordering direction of the ordering term.
     */
    public final @Nullable SQLOrderingDirection orderingDirection;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new SQL ordering term node with the given qualified column name and an ordering direction.
     */
    private SQLOrderingTerm(@Nonnull SQLQualifiedColumn qualifiedColumnName, @Nullable SQLOrderingDirection orderingDirection) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.orderingDirection = orderingDirection;
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLOrderingTerm> transcriber = new Transcriber<SQLOrderingTerm>() {
    
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLOrderingTerm node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.qualifiedColumnName));
            if (node.orderingDirection != null) {
                string.append(" ").append(dialect.transcribe(site, node.orderingDirection));
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLOrderingTerm> getTranscriber() {
        return transcriber;
    }
    
}
