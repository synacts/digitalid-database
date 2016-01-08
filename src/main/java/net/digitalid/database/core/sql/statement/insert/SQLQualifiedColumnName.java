package net.digitalid.database.core.sql.statement.insert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.SQLDialect;
import net.digitalid.database.core.exceptions.operation.FailedValueStoringException;
import net.digitalid.database.core.interfaces.ValueCollector;
import net.digitalid.database.core.sql.SQLParameterizableNode;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 *
 */
public class SQLQualifiedColumnName extends SQLParameterizableNode<SQLQualifiedColumnName> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    public final @Nonnull SQLIdentifier columnName; 
    
    protected SQLQualifiedColumnName(SQLIdentifier columnName) {
        this.columnName = columnName;
    }
    
    /* -------------------------------------------------- Table Name -------------------------------------------------- */
    
    private @Nullable SQLIdentifier tableName;
    
    public void setTableName(@Nullable SQLIdentifier tableName) {
        this.tableName = tableName;
    }

    /* -------------------------------------------------- Value Collector -------------------------------------------------- */
    
    @Override
    public void storeValues(@NonCapturable @Nonnull ValueCollector collector) throws FailedValueStoringException {
        
    }
    
    /* -------------------------------------------------- Transcribe -------------------------------------------------- */
    
    @Override
    public void transcribe(@Nonnull SQLDialect dialect, @Nonnull Site site, @NonCapturable @Nonnull StringBuilder string) throws InternalException {
        string.append(tableName == null ? columnName : tableName + "." + columnName);
    }
    
}
