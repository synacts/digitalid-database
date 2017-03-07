package net.digitalid.database.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.bool.SQLBooleanLiteral;
import net.digitalid.database.dialect.expression.number.SQLCurrentTime;
import net.digitalid.database.dialect.expression.number.SQLVariadicNumberOperator;
import net.digitalid.database.dialect.statement.table.create.SQLType;
import net.digitalid.database.unit.Unit;

/**
 * This class implements the SQLite dialect.
 */
@Stateless
@GenerateSubclass
public abstract class SQLiteDialect extends SQLDialect {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new SQLiteDialectSubclass());
    }
    
    /* -------------------------------------------------- Unparsing -------------------------------------------------- */
    
    @Pure
    protected void unparse(@Nonnull SQLType type, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (type.getType() == CustomType.BINARY128 || type.getType() == CustomType.BINARY256 || type.getType() == CustomType.BINARY) {
            string.append("BLOB");
        } else {
            type.unparse(this, unit, string);
        }
    }
    
    @Pure
    protected void unparse(@Nonnull SQLBooleanLiteral booleanLiteral, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        string.append(booleanLiteral.getValue() ? "1" : "0");
    }
    
    @Pure
    protected void unparse(@Nonnull SQLVariadicNumberOperator variadicNumberOperator, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (variadicNumberOperator ==  SQLVariadicNumberOperator.GREATEST) { string.append("MAX"); }
        else { variadicNumberOperator.unparse(this, unit, string); }
    }
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLType) { unparse((SQLType) node, unit, string); }
        else if (node instanceof SQLBooleanLiteral) { unparse((SQLBooleanLiteral) node, unit, string); }
        else if (node instanceof SQLVariadicNumberOperator) { unparse((SQLVariadicNumberOperator) node, unit, string); }
        else if (node instanceof SQLCurrentTime) { string.append("CAST((JULIANDAY('NOW') - 2440587.5)*86400000 AS INTEGER)"); } // TODO: Is it important that it is the UNIX timestamp? Maybe we could just define another column type.
        else { super.unparse(node, unit, string); }
    }
    
    /* -------------------------------------------------- TODO -------------------------------------------------- */
    
    @Pure
    @TODO(task = "How shall we model this?", date = "2017-03-07", author = Author.KASPAR_ETTER)
    public @Nonnull String PRIMARY_KEY() {
        return "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT";
    }
    
    /* -------------------------------------------------- Index -------------------------------------------------- */
    
    @Pure
    @Deprecated
    public @Nonnull String INDEX(@Nonnull @NonNullableElements @NonEmpty String... columns) {
        return "";
    }
    
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    @SuppressWarnings("StringEquality")
    public void createIndex(@Nonnull Statement statement, @Nonnull String table, @Nonnull @NonNullableElements @NonEmpty String... columns) throws SQLException {
        final @Nonnull StringBuilder string = new StringBuilder("CREATE INDEX IF NOT EXISTS ").append(table).append("_index ON ").append(table).append(" (");
        for (final @Nonnull String column : columns) {
            if (column != columns[0]) { string.append(", "); }
            string.append(column);
        }
        statement.executeUpdate(string.append(")").toString());
    }
    
    /* -------------------------------------------------- Insertions -------------------------------------------------- */
    
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    protected long executeInsert(@Nonnull Statement statement, @Nonnull String SQL) throws SQLException {
        statement.executeUpdate(SQL);
        
        try (@Nonnull ResultSet resultSet = statement.executeQuery("SELECT last_insert_rowid()")) {
            if (resultSet.next()) { return resultSet.getLong(1); }
            else { throw new SQLException("The given SQL statement did not generate any keys."); }
        }
    }
    
}
