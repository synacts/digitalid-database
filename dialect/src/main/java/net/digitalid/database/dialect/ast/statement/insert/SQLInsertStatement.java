package net.digitalid.database.dialect.ast.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 * An AST node holding information about nodes relevant to the SQL insert statement, such as
 * the table name, the column names and the values, which should eventually be inserted.
 * The SQLInsertStatement is a parameterizable node, because its values might be parameterized.
 */
// TODO: re-integrate the SQLValues node, e.g. by adding a Subclass SQLPreparedInsertStatement that does not have SQLValues.
public class SQLInsertStatement implements SQLNode<SQLInsertStatement> {
    
    /* -------------------------------------------------- Table name -------------------------------------------------- */
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyList<? extends SQLColumnName<?>> columnNames;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLInsertStatement(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @NonNullableElements @Frozen ReadOnlyList<? extends SQLColumnName<?>> columnNames) {
        this.qualifiedTableName = qualifiedTableName;
        this.columnNames = columnNames;
    }
    
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
    
    @Override
    public @Nonnull Transcriber<SQLInsertStatement> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- to SQL -------------------------------------------------- */
    
    public String toPreparedStatement(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        return dialect.transcribe(site, this);
    }
    
}
