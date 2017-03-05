package net.digitalid.database.mysql;

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
 * This class implements the MySQL dialect.
 */
@Stateless
@GenerateSubclass
public abstract class MySQLDialect extends SQLDialect {
    
    /**
     * Initializes the dialect.
     */
    @PureWithSideEffects
    @Initialize(target = SQLDialect.class)
    public static void initializeDialect() {
        SQLDialect.instance.set(new MySQLDialectSubclass());
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
//     * Identifiers may in principle begin with a digit but unless quoted may not consist solely of digits.
//     */
//    private static final @Nonnull Pattern PATTERN = Pattern.compile("[a-z_][a-z0-9_$]*");
//    
//    @Pure
//    @Override
//    public boolean isValidIdentifier(@Nonnull String identifier) {
//        return identifier.length() <= 64 && PATTERN.matcher(identifier).matches();
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String PRIMARY_KEY() {
//        return "BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String TINYINT() {
//        return "TINYINT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String BINARY() {
//        return "utf16_bin";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String NOCASE() {
//        return "utf16_general_ci";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String CITEXT() {
//        return "TEXT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String BLOB() {
//        return "LONGBLOB";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String HASH() {
//        return "BINARY(33)";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String VECTOR() {
//        return "BINARY(17)";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String FLOAT() {
//        return "FLOAT";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String DOUBLE() {
//        return "DOUBLE";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String REPLACE() {
//        return "REPLACE";
//    }
//    
//    @Pure
//    @Override
//    public @Nonnull String IGNORE() {
//        return " IGNORE";
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
//        return "UNIX_TIMESTAMP(SYSDATE()) * 1000 + MICROSECOND(SYSDATE(3)) DIV 1000";
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
//    /**
//     * Returns the syntax for creating an index inside a table declaration.
//     * 
//     * @param columns the columns for which the index is to be created.
//     * 
//     * @return the syntax for creating an index inside a table declaration.
//     * 
//     * @require columns.length > 0 : "The columns are not empty.";
//     */
//    @Pure
//    public @Nonnull String INDEX(@Nonnull String... columns) {
//        Require.that(columns.length > 0).orThrow("The columns are not empty.");
//        
//        final @Nonnull StringBuilder string = new StringBuilder(", INDEX(");
//        for (final @Nonnull String column : columns) {
//            if (string.length() != 8) { string.append(", "); }
//            string.append(column);
//        }
//        return string.append(")").toString();
//    }
//    
}
