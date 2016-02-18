package net.digitalid.database.dialect.ast.statement.select;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * This SQL node represents a result column in a select statement.
 */
public class SQLResultColumn implements SQLNode<SQLResultColumn> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified column name.
     */
    public final @Nonnull SQLQualifiedColumnName qualifiedColumnName;
    
    /**
     * The alias of the qualified column name.
     */
    public final @Nullable String alias;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Constructs a new result column node with a given qualified column name and an optional alias.
     */
    private SQLResultColumn(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nullable String alias) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.alias = alias;
    }
    
    /**
     * Returns a result column node with a given qualified column name and an optional alias.
     */
    public static @Nonnull SQLResultColumn get(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nullable String alias) {
        return new SQLResultColumn(qualifiedColumnName, alias);
    }
    
    /* -------------------------------------------------- Transcriber -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLResultColumn> transcriber = new Transcriber<SQLResultColumn>() {
    
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLResultColumn node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.qualifiedColumnName, parameterizable);
            if (node.alias != null) {
                string.append(" AS ");
                string.append(node.alias);
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLResultColumn> getTranscriber() {
        return transcriber;
    }
    
}
