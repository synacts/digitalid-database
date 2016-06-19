package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;

import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.fixes.Brackets;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;

/**
 * Description.
 */
public class SQLCreateTableStatement implements SQLNode<SQLCreateTableStatement> {
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    public final @Nonnull ReadOnlyList<@Nonnull SQLColumnDeclaration> columnDeclarations;
    
    private SQLCreateTableStatement(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull @NonNullableElements ReadOnlyList<SQLColumnDeclaration> columnDeclarations) {
        this.qualifiedTableName = qualifiedTableName;
        this.columnDeclarations = columnDeclarations;
    }
    
    public static @Nonnull SQLCreateTableStatement with(@Nonnull SQLQualifiedTableName qualifiedTableName, @Nonnull ReadOnlyList<@Nonnull SQLColumnDeclaration> columnDeclarations) {
        return new SQLCreateTableStatement(qualifiedTableName, columnDeclarations);
    }
    
    public String toSQL(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        return dialect.transcribe(site, this);
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLCreateTableStatement> transcriber = new Transcriber<SQLCreateTableStatement>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLCreateTableStatement node, @Nonnull Site site) throws InternalException {
            StringBuilder string = new StringBuilder();
            string.append("CREATE TABLE ");
            string.append(node.qualifiedTableName.getValue());
            string.append(" ");
            if (node.columnDeclarations.size() > 0) {
                node.columnDeclarations.map(column -> dialect.transcribe(site, column)).join(Brackets.ROUND);
            }
            return string.toString();
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLCreateTableStatement> getTranscriber() {
        return transcriber;
    }
    
}
