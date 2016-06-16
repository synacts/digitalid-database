package net.digitalid.database.dialect.ast.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.string.iterable.Brackets;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.string.iterable.NonNullableElementConverter;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.elements.NullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * An AST node holding information about nodes relevant to the SQL insert statement, such as
 * the table name, the column names and the values, which should eventually be inserted.
 * The SQLInsertStatement is a parameterizable node, because its values might be parameterized.
 */
public class SQLInsertStatement implements SQLParameterizableNode<SQLInsertStatement> {
    
    /* -------------------------------------------------- Table name -------------------------------------------------- */
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    private final @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLColumnName<?>> columnNames;
    
    /* -------------------------------------------------- Values Or SelectStatement -------------------------------------------------- */
    
    public final @Nonnull SQLValuesOrStatement<?> valuesOrStatement;
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        valuesOrStatement.storeValues(collector);
    }
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLInsertStatement(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLColumnName<?>> columnNames, @Nonnull @NullableElements SQLValuesOrStatement<?> valuesOrStatement) {
        this.qualifiedTableName = qualifiedTableName;
        this.columnNames = columnNames;
        this.valuesOrStatement = valuesOrStatement;
    }
    
    public static @Nonnull SQLInsertStatement get(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @NonNullableElements @Frozen ReadOnlyList<SQLColumnName<?>> columnNames, @Nonnull @NullableElements SQLValuesOrStatement<?> valuesOrStatement) {
        return new SQLInsertStatement(qualifiedTableName, columnNames, valuesOrStatement);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    private static class SQLColumnConverter implements NonNullableElementConverter<SQLColumnName<?>> {
         
        private final @Nonnull SQLDialect dialect;
        private final @Nonnull Site site;
        
        SQLColumnConverter(@Nonnull SQLDialect dialect, @Nonnull Site site) {
            this.dialect = dialect;
            this.site = site;
        }
        
        // TODO: the ElementConverter should also use the string builder instead of creating a new string everytime.
        @Override
        public @Nonnull String toString(@Nonnull SQLColumnName<?> element) {
            StringBuilder string = new StringBuilder();
            try {
                dialect.transcribe(site, string, element, false);
            } catch (InternalException e) {
                e.printStackTrace();
            }
            return string.toString();
        }
        
    }
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLInsertStatement> transcriber = new Transcriber<SQLInsertStatement>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLInsertStatement node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            string.append("INSERT INTO ");
            dialect.transcribe(site, string, node.qualifiedTableName, false);
            string.append(" ");
            string.append(IterableConverter.toString(node.columnNames, new SQLColumnConverter(dialect, site), Brackets.ROUND, ","));
            string.append(" ");
            dialect.transcribe(site, string, node.valuesOrStatement, true);
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLInsertStatement> getTranscriber() {
        return transcriber;
    }
    
    /* -------------------------------------------------- to SQL -------------------------------------------------- */
    
    public String toPreparedStatement(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        dialect.transcribe(site, stringBuilder, this, true);
        return stringBuilder.toString();
    }
    
}
