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
 * This SQL node represents a result column in a select statement.
 */
public class SQLResultColumn implements SQLNode<SQLResultColumn> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified column name.
     */
    public final @Nonnull SQLQualifiedColumn qualifiedColumnName;
    
    /**
     * The alias of the qualified column name.
     */
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new result column node with a given qualified column name and an optional alias.
     */
    private SQLResultColumn(@Nonnull SQLQualifiedColumn qualifiedColumnName, @Nullable String alias) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.alias = alias;
    }
    
    /**
     * Returns a result column node with a given qualified column name and an optional alias.
     */
    @Pure
    public static @Nonnull SQLResultColumn get(@Nonnull SQLQualifiedColumn qualifiedColumnName, @Nullable String alias) {
        return new SQLResultColumn(qualifiedColumnName, alias);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLResultColumn> transcriber = new Transcriber<SQLResultColumn>() {
    
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLResultColumn node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.qualifiedColumnName));
            if (node.alias != null) {
                string.append(" AS ");
                string.append(node.alias);
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLResultColumn> getTranscriber() {
        return transcriber;
    }
    
}
