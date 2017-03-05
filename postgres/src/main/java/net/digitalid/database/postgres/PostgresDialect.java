package net.digitalid.database.postgres;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.method.PureWithSideEffects;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.collaboration.annotations.TODO;
import net.digitalid.utility.collaboration.enumerations.Author;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.initialization.annotations.Initialize;
import net.digitalid.utility.validation.annotations.type.Stateless;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.SQLNode;
import net.digitalid.database.unit.Unit;

/**
 * This class implements the PostgreSQL dialect.
 */
@Stateless
@GenerateSubclass
public abstract class PostgresDialect extends SQLDialect {
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new PostgresDialectSubclass());
    }
    
    @Pure
    @Override
    @TODO(task = "Write the implementation.", date = "2017-03-05", author = Author.KASPAR_ETTER)
    public void unparse(@Nonnull SQLNode node, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        
    }
    
//    /* -------------------------------------------------- Syntax -------------------------------------------------- */
//    
//    /**
//     * The pattern that valid database identifiers have to match.
//     */
//    private static final @Nonnull Pattern PATTERN = Pattern.compile("[a-z_][a-z0-9_$]*", Pattern.CASE_INSENSITIVE);
//    
//    @Pure
//    @Override
//    public boolean isValidIdentifier(@Nonnull String identifier) {
//        return identifier.length() <= 63 && PATTERN.matcher(identifier).matches();
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String PRIMARY_KEY() {
//        return "BIGSERIAL PRIMARY KEY";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String TINYINT() {
//        return "SMALLINT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String BINARY() {
//        return "\"en_US.UTF-8\"";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String NOCASE() {
//        return "\"en_US.UTF-8\"";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String CITEXT() {
//        return "CITEXT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String BLOB() {
//        return "BYTEA";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String HASH() {
//        return "BYTEA";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String VECTOR() {
//        return "BYTEA";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String FLOAT() {
//        return "FLOAT4";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String DOUBLE() {
//        return "FLOAT8";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String REPLACE() {
//        return "INSERT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String IGNORE() {
//        return "";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String GREATEST() {
//        return "GREATEST";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String CURRENT_TIME() {
//        return "ROUND(EXTRACT(EPOCH FROM CLOCK_TIMESTAMP()) * 1000)";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String BOOLEAN(boolean value) {
//        return Boolean.toString(value);
//    }
//    
//    /* -------------------------------------------------- Index -------------------------------------------------- */
//    
//    @Pure
//    @Override
//    public @Nonnull String INDEX(@Nonnull String... columns) {
//        Require.that(columns.length > 0).orThrow("The columns are not empty.");
//        
//        return "";
//    }
//    
//    @Override
//    @NonCommitting
//    @SuppressWarnings("StringEquality")
//    public void createIndex(@Nonnull Statement statement, @Nonnull String table, @Nonnull String... columns) throws FailedUpdateExecutionException {
//        Require.that(columns.length > 0).orThrow("The columns are not empty.");
//        
//        final @Nonnull StringBuilder string = new StringBuilder("DO $$ DECLARE counter INTEGER; BEGIN ");
//        string.append("SELECT COUNT(*) INTO counter FROM pg_indexes WHERE schemaname = 'public' AND tablename = '").append(table).append("' AND indexname = '").append(table).append("_index").append("';");
//        string.append("IF counter = 0 THEN EXECUTE 'CREATE INDEX ").append(table).append("_index ON ").append(table).append(" (");
//        for (final @Nonnull String column : columns) {
//            if (column != columns[0]) { string.append(", "); }
//            string.append(column);
//        }
//        string.append(")'; END IF; END; $$");
//        try { statement.execute(string.toString()); } catch (@Nonnull SQLException exception) { throw FailedUpdateExecutionException.get(exception); }
//    }
//    
//    /* -------------------------------------------------- Ignoring -------------------------------------------------- */
//    
//    /**
//     * Creates a rule to ignore duplicate insertions.
//     * 
//     * @param statement a statement to create the rule with.
//     * @param table the table to which the rule is applied.
//     * @param columns the columns of the primary key.
//     * 
//     * @require columns.length > 0 : "The columns are not empty.";
//     */
//    @NonCommitting
//    protected void onInsertIgnore(@Nonnull Statement statement, @Nonnull String table, @Nonnull String... columns) throws FailedUpdateExecutionException {
//        Require.that(columns.length > 0).orThrow("The columns are not empty.");
//        
//        final @Nonnull StringBuilder string = new StringBuilder("CREATE OR REPLACE RULE ").append(table).append("_on_insert_ignore ");
//        string.append("AS ON INSERT TO ").append(table).append(" WHERE EXISTS(SELECT 1 FROM ").append(table).append(" WHERE (");
//        boolean first = true;
//        for (final @Nonnull String column : columns) {
//            if (first) { first = false; }
//            else { string.append(", "); }
//            string.append(column);
//        }
//        string.append(") = (");
//        first = true;
//        for (final @Nonnull String column : columns) {
//            if (first) { first = false; }
//            else { string.append(", "); }
//            string.append("NEW.").append(column);
//        }
//        string.append(")) DO INSTEAD NOTHING");
//        try { statement.executeUpdate(string.toString()); } catch (@Nonnull SQLException exception) { throw FailedUpdateExecutionException.get(exception); }
//    }
//    
//    /**
//     * Drops the rule to ignore duplicate insertions.
//     * 
//     * @param statement a statement to drop the rule with.
//     * @param table the table from which the rule is dropped.
//     */
//    @NonCommitting
//    protected void onInsertNotIgnore(@Nonnull Statement statement, @Nonnull String table) throws FailedUpdateExecutionException {
//        try { statement.executeUpdate("DROP RULE IF EXISTS " + table + "_on_insert_ignore ON " + table); } catch (@Nonnull SQLException exception) { throw FailedUpdateExecutionException.get(exception); }
//    }
//    
//    /* -------------------------------------------------- Updating -------------------------------------------------- */
//    
//    /**
//     * Creates a rule to update duplicate insertions.
//     * 
//     * @param statement a statement to create the rule with.
//     * @param table the table to which the rule is applied.
//     * @param key the number of columns in the primary key.
//     * @param columns the columns which are inserted starting with the columns of the primary key.
//     * 
//     * @require columns.length >= key : "At least as many columns as in the primary key are provided.";
//     */
//    @NonCommitting
//    protected void onInsertUpdate(@Nonnull Statement statement, @Nonnull String table, int key, @Nonnull String... columns) throws FailedUpdateExecutionException {
//        Require.that(key > 0).orThrow("The number of columns in the primary key is positive.");
//        Require.that(columns.length >= key).orThrow("At least as many columns as in the primary key are provided.");
//        
//        final @Nonnull StringBuilder string = new StringBuilder("CREATE OR REPLACE RULE ").append(table).append("_on_insert_update ");
//        string.append("AS ON INSERT TO ").append(table).append(" WHERE EXISTS(SELECT 1 FROM ").append(table);
//        
//        final @Nonnull StringBuilder condition = new StringBuilder(" WHERE (");
//        for (int i = 0; i < key; i++) {
//            if (i > 0) { condition.append(", "); }
//            condition.append(columns[i]);
//        }
//        condition.append(") = (");
//        for (int i = 0; i < key; i++) {
//            if (i > 0) { condition.append(", "); }
//            condition.append("NEW.").append(columns[i]);
//        }
//        condition.append(")");
//        
//        string.append(condition).append(") DO INSTEAD UPDATE ").append(table).append(" SET ");
//        for (int i = key; i < columns.length; i++) {
//            if (i > key) { string.append(", "); }
//            string.append(columns[i]).append(" = NEW.").append(columns[i]);
//        }
//        string.append(condition);
//        try { statement.executeUpdate(string.toString()); } catch (@Nonnull SQLException exception) { throw FailedUpdateExecutionException.get(exception); }
//    }
//    
//    /**
//     * Drops the rule to update duplicate insertions.
//     * 
//     * @param statement a statement to drop the rule with.
//     * @param table the table from which the rule is dropped.
//     */
//    @NonCommitting
//    protected void onInsertNotUpdate(@Nonnull Statement statement, @Nonnull String table) throws FailedUpdateExecutionException {
//        try { statement.executeUpdate("DROP RULE IF EXISTS " + table + "_on_insert_update ON " + table); } catch (@Nonnull SQLException exception) { throw FailedUpdateExecutionException.get(exception); }
//    }
//    
}
