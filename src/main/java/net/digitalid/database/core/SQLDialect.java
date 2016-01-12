package net.digitalid.database.core;

import javax.annotation.Nonnull;
import net.digitalid.database.core.sql.expression.SQLBinaryExpression;
import net.digitalid.database.core.sql.expression.SQLExpression;
import net.digitalid.database.core.sql.expression.SQLUnaryExpression;
import net.digitalid.database.core.sql.expression.SQLVariadicExpression;
import net.digitalid.database.core.sql.expression.bool.SQLBinaryBooleanOperator;
import net.digitalid.database.core.sql.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.core.sql.expression.bool.SQLComparisonOperator;
import net.digitalid.database.core.sql.expression.bool.SQLUnaryBooleanOperator;
import net.digitalid.database.core.sql.expression.number.SQLBinaryNumberOperator;
import net.digitalid.database.core.sql.expression.number.SQLNumberLiteral;
import net.digitalid.database.core.sql.expression.number.SQLUnaryNumberOperator;
import net.digitalid.database.core.sql.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.core.sql.expression.string.SQLStringLiteral;
import net.digitalid.database.core.sql.expression.string.SQLVariadicStringOperator;
import net.digitalid.database.core.sql.identifier.SQLIdentifier;
import net.digitalid.database.core.sql.statement.insert.SQLInsertStatement;
import net.digitalid.database.core.sql.statement.select.SQLSelectStatement;
import net.digitalid.database.core.sql.statement.table.create.SQLType;
import net.digitalid.database.core.sql.SQLNode;
import net.digitalid.database.core.table.Site;
import net.digitalid.utility.annotations.reference.NonCapturable;
import net.digitalid.utility.annotations.state.Immutable;
import net.digitalid.utility.exceptions.internal.InternalException;

/**
 * A dialect implements a particular version of the structured query language (SQL).
 */
@Immutable
public abstract class SQLDialect {
    
    /* -------------------------------------------------- Literals -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLBooleanLiteral literal) throws InternalException {
        string.append(literal.getValue() ? "TRUE" : "FALSE");
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLNumberLiteral literal) throws InternalException {
        string.append(literal.getValue());
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLStringLiteral literal) throws InternalException {
        string.append("\"").append(literal.getValue()).append("\"");
    }
    
    /* -------------------------------------------------- Unary Expressions -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLUnaryExpression<?, ?> unaryExpression) throws InternalException {
        unaryExpression.getOperator().transcribe(this, site, string);
        string.append("(");
        unaryExpression.getExpression().transcribe(this, site, string);
        string.append(")");
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLUnaryBooleanOperator operator) throws InternalException {
        switch (operator) {
            case NOT: string.append("NOT"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLUnaryNumberOperator operator) throws InternalException {
        switch (operator) {
            case ROUND: string.append("ROUND"); break;
            case NEGATE: string.append("-"); break;
            case ABSOLUTE: string.append("ABS"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /* -------------------------------------------------- Binary Expressions -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLBinaryExpression<?, ?> binaryExpression) throws InternalException {
        string.append("(");
        binaryExpression.getLeftExpression().transcribe(this, site, string);
        string.append(") ");
        binaryExpression.getOperator().transcribe(this, site, string);
        string.append(" (");
        binaryExpression.getRightExpression().transcribe(this, site, string);
        string.append(")");
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLBinaryBooleanOperator operator) throws InternalException {
        switch (operator) {
            case AND: string.append("AND"); break;
            case OR: string.append("OR"); break;
            case XOR: string.append("XOR"); break;
            case EQUAL: string.append("="); break;
            case UNEQUAL: string.append("!="); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLBinaryNumberOperator operator) throws InternalException {
        switch (operator) {
            case ADDITION: string.append("+"); break;
            case SUBTRACTION: string.append("-"); break;
            case MULTIPLICATION: string.append("*"); break;
            case DIVISION: string.append("/"); break;
            case INTEGER_DIVISION: string.append("DIV"); break;
            case MODULO: string.append("%"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
    }
    
    /* -------------------------------------------------- Variadic Expressions -------------------------------------------------- */
    
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
    
    /* -------------------------------------------------- Boolean Expressions -------------------------------------------------- */
    
    /**
     * Transcribes the given node to this dialect at the given site.
     */
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLComparisonOperator operator) throws InternalException {
        switch (operator) {
            case EQUAL: string.append("="); break;
            case UNEQUAL: string.append("!="); break;
            case GREATER_OR_EQUAL: string.append(">="); break;
            case GREATER: string.append(">"); break;
            case LESS_OR_EQUAL: string.append("<="); break;
            case LESS: string.append("<"); break;
            default: throw InternalException.get(operator.name() + " not implemented.");
        }
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
    
    public void transcribe(@Nonnull Site site, @NonCapturable @Nonnull StringBuilder string, @Nonnull SQLNode<?> node) throws InternalException {
        node.transcribe(this, site, string);
    }
    
}
