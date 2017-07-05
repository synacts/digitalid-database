package net.digitalid.database.postgres;

import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.contracts.Require;
import net.digitalid.utility.conversion.model.CustomType;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.math.Positive;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.annotations.transaction.NonCommitting;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.dialect.expression.number.SQLCurrentTime;
import net.digitalid.database.dialect.statement.table.create.SQLType;

/**
 * This class implements the PostgreSQL dialect.
 */
@Stateless
@GenerateSubclass
public abstract class PostgresDialect extends SQLDialect {
    
    /* -------------------------------------------------- Initialization -------------------------------------------------- */
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new PostgresDialectSubclass());
    }
    
    /* -------------------------------------------------- Unparsing -------------------------------------------------- */
    
    @Pure
    protected void unparse(@Nonnull SQLType type, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (type.getType() == CustomType.INTEGER08) {
            string.append("SMALLINT");
        } else if (type.getType() == CustomType.DECIMAL32) {
            string.append("FLOAT4");
        } else if (type.getType() == CustomType.DECIMAL64) {
            string.append("FLOAT8");
        } else if (type.getType() == CustomType.INTEGER08) {
            string.append("SMALLINT");
        } else if (type.getType() == CustomType.BINARY128 || type.getType() == CustomType.BINARY256 || type.getType() == CustomType.BINARY) {
            string.append("BYTEA");
        } else {
            type.unparse(this, unit, string);
            if (type.getType() == CustomType.STRING64 || type.getType() == CustomType.STRING128 || type.getType() == CustomType.STRING) {
                string.append(" COLLATE \"en_US.UTF-8\"");
            }
        }
        
    }
    
    @Pure
    @Override
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        if (node instanceof SQLType) { unparse((SQLType) node, unit, string); }
        else if (node instanceof SQLCurrentTime) { string.append("ROUND(EXTRACT(EPOCH FROM CLOCK_TIMESTAMP()) * 1000)"); } // TODO: Is it important that it is the UNIX timestamp? Maybe we could just define another column type.
        else { super.unparse(node, unit, string); }
    }
    
    /* -------------------------------------------------- TODO -------------------------------------------------- */
    
    @Pure
    @TODO(task = "How shall we model this?", date = "2017-03-07", author = Author.KASPAR_ETTER)
    public @Nonnull String PRIMARY_KEY() {
        return "BIGSERIAL PRIMARY KEY";
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
        final @Nonnull StringBuilder string = new StringBuilder("DO $$ DECLARE counter INTEGER; BEGIN ");
        string.append("SELECT COUNT(*) INTO counter FROM pg_indexes WHERE schemaname = 'public' AND tablename = '").append(table).append("' AND indexname = '").append(table).append("_index").append("';");
        string.append("IF counter = 0 THEN EXECUTE 'CREATE INDEX ").append(table).append("_index ON ").append(table).append(" (");
        for (final @Nonnull String column : columns) {
            if (column != columns[0]) { string.append(", "); }
            string.append(column);
        }
        string.append(")'; END IF; END; $$");
        statement.execute(string.toString());
    }
    
    /* -------------------------------------------------- Ignoring -------------------------------------------------- */
    
    /**
     * Creates a rule to ignore duplicate insertions.
     * 
     * @param statement a statement to create the rule with.
     * @param table the table to which the rule is applied.
     * @param columns the columns of the primary key.
     */
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    protected void onInsertIgnore(@Nonnull Statement statement, @Nonnull String table, @NonNullableElements @NonEmpty @Nonnull String... columns) throws SQLException {
        final @Nonnull StringBuilder string = new StringBuilder("CREATE OR REPLACE RULE ").append(table).append("_on_insert_ignore ");
        string.append("AS ON INSERT TO ").append(table).append(" WHERE EXISTS(SELECT 1 FROM ").append(table).append(" WHERE (");
        boolean first = true;
        for (final @Nonnull String column : columns) {
            if (first) { first = false; }
            else { string.append(", "); }
            string.append(column);
        }
        string.append(") = (");
        first = true;
        for (final @Nonnull String column : columns) {
            if (first) { first = false; }
            else { string.append(", "); }
            string.append("NEW.").append(column);
        }
        string.append(")) DO INSTEAD NOTHING");
        statement.executeUpdate(string.toString());
    }
    
    /**
     * Drops the rule to ignore duplicate insertions.
     * 
     * @param statement a statement to drop the rule with.
     * @param table the table from which the rule is dropped.
     */
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    protected void onInsertNotIgnore(@Nonnull Statement statement, @Nonnull String table) throws SQLException {
        statement.executeUpdate("DROP RULE IF EXISTS " + table + "_on_insert_ignore ON " + table);
    }
    
    /* -------------------------------------------------- Updating -------------------------------------------------- */
    
    /**
     * Creates a rule to update duplicate insertions.
     * 
     * @param statement a statement to create the rule with.
     * @param table the table to which the rule is applied.
     * @param key the number of columns in the primary key.
     * @param columns the columns which are inserted starting with the columns of the primary key.
     * 
     * @require columns.length >= key : "At least as many columns as in the primary key are provided.";
     */
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    protected void onInsertUpdate(@Nonnull Statement statement, @Nonnull String table, @Positive int key, @Nonnull @NonNullableElements @NonEmpty String... columns) throws SQLException {
        Require.that(columns.length >= key).orThrow("At least as many columns as in the primary key are provided.");
        
        final @Nonnull StringBuilder string = new StringBuilder("CREATE OR REPLACE RULE ").append(table).append("_on_insert_update ");
        string.append("AS ON INSERT TO ").append(table).append(" WHERE EXISTS(SELECT 1 FROM ").append(table);
        
        final @Nonnull StringBuilder condition = new StringBuilder(" WHERE (");
        for (int i = 0; i < key; i++) {
            if (i > 0) { condition.append(", "); }
            condition.append(columns[i]);
        }
        condition.append(") = (");
        for (int i = 0; i < key; i++) {
            if (i > 0) { condition.append(", "); }
            condition.append("NEW.").append(columns[i]);
        }
        condition.append(")");
        
        string.append(condition).append(") DO INSTEAD UPDATE ").append(table).append(" SET ");
        for (int i = key; i < columns.length; i++) {
            if (i > key) { string.append(", "); }
            string.append(columns[i]).append(" = NEW.").append(columns[i]);
        }
        string.append(condition);
        statement.executeUpdate(string.toString());
    }
    
    /**
     * Drops the rule to update duplicate insertions.
     * 
     * @param statement a statement to drop the rule with.
     * @param table the table from which the rule is dropped.
     */
    @Deprecated
    @NonCommitting
    @PureWithSideEffects
    protected void onInsertNotUpdate(@Nonnull Statement statement, @Nonnull String table) throws SQLException {
        statement.executeUpdate("DROP RULE IF EXISTS " + table + "_on_insert_update ON " + table);
    }
    
}
