package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedTableName;
import net.digitalid.utility.collections.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.freezable.FreezableArrayList;
import net.digitalid.utility.collections.freezable.FreezableList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.string.iterable.Brackets;
import net.digitalid.utility.string.iterable.IterableConverter;
import net.digitalid.utility.string.iterable.NonNullableElementConverter;
import net.digitalid.utility.validation.reference.NonCapturable;

/**
 * Description.
 */
public class SQLCreateTableStatement implements SQLNode<SQLCreateTableStatement> {
    
    public final @Nonnull SQLQualifiedTableName qualifiedTableName;
    
    public final @Nonnull @NonNullableElements FreezableList<SQLColumnDeclaration> columnDeclarations = FreezableArrayList.get();
    
    private SQLCreateTableStatement(@Nonnull SQLQualifiedTableName qualifiedTableName) {
        this.qualifiedTableName = qualifiedTableName;
    }
    
    public static @Nonnull SQLCreateTableStatement with(@Nonnull SQLQualifiedTableName qualifiedTableName) {
        return new SQLCreateTableStatement(qualifiedTableName);
    }
    
    public String toSQL(@Nonnull SQLDialect dialect, @Nonnull Site site) throws InternalException {
        final @Nonnull StringBuilder stringBuilder = new StringBuilder();
        getTranscriber().transcribeNode(dialect, this, site, stringBuilder);
        return stringBuilder.toString();
    }
    
    /* -------------------------------------------------- SQLNode -------------------------------------------------- */
    
    private static class SQLColumnDeclarationsConverter implements NonNullableElementConverter<SQLColumnDeclaration> {
         
        private final @Nonnull SQLDialect dialect;
        private final @Nonnull Site site;
        
        SQLColumnDeclarationsConverter(@Nonnull SQLDialect dialect, @Nonnull Site site) {
            this.dialect = dialect;
            this.site = site;
        }
        
        // TODO: the ElementConverter should also use the stringbuilder instead of creating a new string everytime.
        @Override
        public @Nonnull String toString(@Nonnull SQLColumnDeclaration element) {
            StringBuilder string = new StringBuilder();
            try {
                element.getTranscriber().transcribeNode(dialect, element, site, string);
            } catch (InternalException e) {
                e.printStackTrace();
            }
            return string.toString();
        }
        
    }
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLCreateTableStatement> transcriber = new Transcriber<SQLCreateTableStatement>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLCreateTableStatement node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            string.append("CREATE TABLE ");
            string.append(node.qualifiedTableName);
            string.append(" ");
            IterableConverter.toString(node.columnDeclarations, new SQLColumnDeclarationsConverter(dialect, site), Brackets.ROUND, ",");
            string.append("(");
            string.append(")");
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLCreateTableStatement> getTranscriber() {
        return transcriber;
    }
    
    public void addColumnDeclaration(@Nonnull SQLColumnDeclaration columnDeclaration) {
        columnDeclarations.add(columnDeclaration);
    }
}
