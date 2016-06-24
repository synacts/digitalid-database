package net.digitalid.database.dialect.ast.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 * An AST node holding information about nodes relevant to the SQL insert statement, such as
 * the table name and the column names, which should eventually be inserted.
 */
// TODO: re-integrate the SQLValues node, e.g. by adding a Subclass SQLPreparedInsertStatement that does not have SQLValues.
public class SQLInsertStatement implements SQLNode<SQLInsertStatement> {
    
    /* -------------------------------------------------- Table name -------------------------------------------------- */
    
    /**
     * The qualified table name.
     */
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    /**
     * A list of column names.
     */
    private final @Nonnull ImmutableList<@Nonnull ? extends SQLColumnName<?>> columnNames;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL insert statement node.
     */
    private SQLInsertStatement(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @Frozen ReadOnlyList<@Nonnull ? extends SQLColumnName<?>> columnNames) {
        this.qualifiedTableName = qualifiedTableName;
        this.columnNames = ImmutableList.with(columnNames);
    }
    
    /**
     * Returns an SQL insert statement node.
     */
    @Pure
    public static @Nonnull SQLInsertStatement get(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @NonNullableElements @Frozen ReadOnlyList<? extends SQLColumnName<?>> columnNames) {
        return new SQLInsertStatement(qualifiedTableName, columnNames);
    }
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLInsertStatement> transcriber = new Transcriber<SQLInsertStatement>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLInsertStatement node, @Nonnull Site site) throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append("INSERT INTO ");
            string.append(dialect.transcribe(site, node.qualifiedTableName));
            string.append(" ");
            string.append(node.columnNames.map((columnName) -> dialect.transcribe(site, columnName)).join(Brackets.ROUND));
            string.append(" VALUES ");
            string.append(node.columnNames.map((columnName) -> '?').join(Brackets.ROUND));
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLInsertStatement> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- to SQL -------------------------------------------------- */
    
    /**
     * Returns a string representation of the prepared statement.
     */
    @Pure
    public String toPreparedStatement(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        return dialect.transcribe(site, this);
    }
    
}
