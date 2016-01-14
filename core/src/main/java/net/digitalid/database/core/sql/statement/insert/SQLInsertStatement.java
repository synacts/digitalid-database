package net.digitalid.database.core.sql.statement.insert;

import javax.annotation.Nonnull;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.identifier.SQLQualifiedTableName;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.validation.reference.NonCapturable;
import net.digitalid.utility.validation.state.Pure;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.freezable.Frozen;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * An AST node holding information about nodes relevant to the SQL insert statement, such as
 * the table name, the column names and the values, which should eventually be inserted.
 * The SQLInsertStatement is a parameterizable node, because its values might be parameterized.
 */
public class SQLInsertStatement extends SQLParameterizableNode<SQLInsertStatement> {
    
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
    
    /* -------------------------------------------------- Transcribe -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        string.append("INSERT OR IGNORE INTO ");
        dialect.transcribe(site, string, qualifiedTableName);
        string.append("(");
        for (SQLQualifiedColumnName qualifiedColumnName : qualifiedColumnNames) {
            dialect.transcribe(site, string, qualifiedColumnName);
            string.append(",");
        }
        string.append(")");
    }

    /* -------------------------------------------------- Value Collector -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        valuesOrStatement.storeValues(collector);
    }
    
}
