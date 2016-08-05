package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.immutable.ImmutableList;

import net.digitalid.database.storage.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 * An AST node holding information about nodes relevant to the SQL create table statement, such as
 * the table name and the column declarations.
 */
public class SQLCreateTableStatement implements SQLNode<SQLCreateTableStatement> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The qualified table name.
     */
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    /**
     * The column declarations.
     */
    public final @Nonnull ImmutableList<@Nonnull SQLColumnDeclaration> columnDeclarations;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new create table statement node with the given qualified table name and the column declarations.
     */
    private SQLCreateTableStatement(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull ReadOnlyList<@Nonnull SQLColumnDeclaration> columnDeclarations) {
        this.qualifiedTableName = qualifiedTableName;
        this.columnDeclarations = ImmutableList.withElementsOf(columnDeclarations);
    }
    
    /**
     * Returns a create table statement node with the given qualified table name and the column declarations.
     */
    @Pure
    public static @Nonnull SQLCreateTableStatement with(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull ReadOnlyList<@Nonnull SQLColumnDeclaration> columnDeclarations) {
        return new SQLCreateTableStatement(qualifiedTableName, columnDeclarations);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that returns a string representation of this SQL node.
     */
    private static final @Nonnull Transcriber<SQLCreateTableStatement> transcriber = new Transcriber<SQLCreateTableStatement>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLCreateTableStatement node, @Nonnull Site site) throws InternalException {
            StringBuilder string = new StringBuilder();
            string.append("CREATE TABLE ");
            string.append(dialect.transcribe(site, node.qualifiedTableName));
            string.append(" ");
            if (node.columnDeclarations.size() > 0) {
                string.append(node.columnDeclarations.map(column -> dialect.transcribe(site, column)).join(Brackets.ROUND));
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLCreateTableStatement> getTranscriber() {
        return transcriber;
    }
    
}
