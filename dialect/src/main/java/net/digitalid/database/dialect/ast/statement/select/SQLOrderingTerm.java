package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;

/**
 * This SQL node represents the ordering term of an SQL select statement.
 */
public class SQLOrderingTerm implements SQLNode<SQLOrderingTerm> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified column name of the ordering term
     */
    public final @Nonnull SQLQualifiedColumnName qualifiedColumnName;
    
    /**
     * The ordering direction of the ordering term.
     */
    public final @Nullable SQLOrderingDirection orderingDirection;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new SQL ordering term node with the given qualified column name and an ordering direction.
     */
    private SQLOrderingTerm(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nullable SQLOrderingDirection orderingDirection) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.orderingDirection = orderingDirection;
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLOrderingTerm> transcriber = new Transcriber<SQLOrderingTerm>() {
    
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLOrderingTerm node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.qualifiedColumnName, parameterizable);
            if (node.orderingDirection != null) {
                string.append(" ");
                dialect.transcribe(site, string, node.orderingDirection, parameterizable);
            }
        }
        
    };
    
    @Nonnull @Override public Transcriber<SQLOrderingTerm> getTranscriber() {
        return transcriber;
    }
    
}
