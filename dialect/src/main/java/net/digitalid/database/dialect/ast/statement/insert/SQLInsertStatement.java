package net.digitalid.database.dialect.ast.statement.insert;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.freezable.annotations.Frozen;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Pure;

import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.database.exceptions.operation.FailedValueStoringException;

/**
 * An AST node holding information about nodes relevant to the SQL insert statement, such as
 * the table name, the column names and the values, which should eventually be inserted.
 * The SQLInsertStatement is a parameterizable node, because its values might be parameterized.
 */
public class SQLInsertStatement implements SQLParameterizableNode<SQLInsertStatement> {
    
    /* -------------------------------------------------- Public Final Fields  -------------------------------------------------- */
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    private SQLInsertStatement(@Nonnull SQLQualifiedTableName qualifiedTableName) {
        this.qualifiedColumnNames = FreezableArrayList.get();
        this.qualifiedTableName = qualifiedTableName;
    }
    
    public static @Nonnull SQLInsertStatement get(@Nonnull SQLQualifiedTableName qualifiedTableName) {
        return new SQLInsertStatement(qualifiedTableName);
    }
    
    /* -------------------------------------------------- Column Names -------------------------------------------------- */
    
    private final @Nonnull @NonNullableElements FreezableArrayList<SQLQualifiedColumnName> qualifiedColumnNames;
    
    @Pure
    public @Nonnull @Frozen ReadOnlyList<SQLQualifiedColumnName> getQualifiedColumnNames() {
        return qualifiedColumnNames.freeze();
    }
    
    public void addQualifiedColumnName(@Nonnull SQLQualifiedColumnName qualifiedColumnName) {
        qualifiedColumnNames.add(qualifiedColumnName);
    }
    
    /* -------------------------------------------------- Values Or SelectStatement -------------------------------------------------- */
    
    private @Nonnull SQLValuesOrStatement valuesOrStatement;
    
    @Pure
    public @Nonnull SQLValuesOrStatement getValuesOrStatement() {
        return valuesOrStatement;
    }
    
    public void setValuesOrStatement(@Nonnull SQLValuesOrStatement valuesOrStatement) {
        this.valuesOrStatement = valuesOrStatement;
    }
    
    /* -------------------------------------------------- Value Collector -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        valuesOrStatement.storeValues(collector);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLInsertStatement> transcriber = new Transcriber<SQLInsertStatement>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLInsertStatement node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            string.append("INSERT OR IGNORE INTO ");
            dialect.transcribe(site, string, node.qualifiedTableName);
            string.append("(");
            for (SQLQualifiedColumnName qualifiedColumnName : node.qualifiedColumnNames) {
                dialect.transcribe(site, string, qualifiedColumnName);
                string.append(",");
            }
            string.append(")");
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLInsertStatement> getTranscriber() {
        return transcriber;
    }
    
}
