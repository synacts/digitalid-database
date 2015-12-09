package net.digitalid.database.core;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.database.core.sql.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.core.sql.expression.string.SQLVariadicStringOperator;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.system.exceptions.InternalException;

/**
 * A dialect implements a particular version of the structured query language (SQL).
 */
@Immutable
public abstract class SQLDialect {
    
    /* -------------------------------------------------- Expression -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLVariadicExpression<?, ?> variadicExpression) throws InternalException {
        variadicExpression.getOperator().transcribe(this, site, string);
        string.append("(");
        boolean first = true;
        for (final @Nonnull SQLExpression expression : variadicExpression.getExpressions()) {
            if (!first) { string.append(", "); } else { first = false; }
            expression.transcribe(this, site, string);
        }
        string.append(")");
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLVariadicNumberOperator operator) throws InternalException {
        switch (operator) {
            case GREATEST: string.append("GREATEST"); break;
            case COALESCE: string.append("COALESCE"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLVariadicStringOperator operator) throws InternalException {
        switch (operator) {
            case CONCAT: string.append("CONCAT"); break;
            case GREATEST: string.append("GREATEST"); break;
            case COALESCE: string.append("COALESCE"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /* -------------------------------------------------- Identifier -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLIdentifier identifier) throws InternalException {
        string.append("\"").append(identifier.getValue()).append("\"");
    }
    
    /* -------------------------------------------------- Statements -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLType type) throws InternalException {
        switch (type) {
            case EMPTY: string.append("BOOLEAN"); break;
            case BOOLEAN: string.append("BOOLEAN"); break;
            case INTEGER08: string.append("TINYINT"); break;
            case INTEGER16: string.append("SMALLINT"); break;
            case INTEGER32: string.append("INT"); break;
            case INTEGER64: string.append("BIGINT"); break;
            case INTEGER: string.append("BLOB"); break;
            case DECIMAL32: string.append("FLOAT"); break;
            case DECIMAL64: string.append("DOUBLE"); break;
            case STRING01: string.append("CHAR(1)"); break;
            case STRING64: string.append("VARCHAR(64) COLLATE utf16_bin"); break;
            case STRING: string.append("TEXT"); break;
            case BINARY128: string.append("BINARY(16)"); break;
            case BINARY256: string.append("BINARY(32)"); break;
            case BINARY: string.append("MEDIUMBLOB"); break;
            default: throw InternalException.get(type.name() + " not implemented.");
        }
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLSelectStatement selectStatement) throws InternalException {
        // TODO
    }
    
}
