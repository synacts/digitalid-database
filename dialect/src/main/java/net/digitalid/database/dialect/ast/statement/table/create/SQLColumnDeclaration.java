package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLQualifiedColumnName;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.collections.readonly.ReadOnlySet;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

/**
 *
 */
public class SQLColumnDeclaration implements SQLNode<SQLColumnDeclaration> {
    
    public final @Nonnull SQLQualifiedColumnName qualifiedColumnName;
    
    public final @Nonnull SQLType type;
    
    public final @Nullable @NonNullableElements ReadOnlySet<SQLColumnConstraint> columnConstraint;
    
    private SQLColumnDeclaration(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nonnull SQLType type, @Nullable @NonNullableElements ReadOnlySet<SQLColumnConstraint> columnConstraint) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.type = type;
        this.columnConstraint = columnConstraint;
    }
    
    public static @Nonnull SQLColumnDeclaration of(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nonnull SQLType type) {
        return new SQLColumnDeclaration(qualifiedColumnName, type, null);
    }
    
    public static @Nonnull SQLColumnDeclaration of(@Nonnull SQLQualifiedColumnName qualifiedColumnName, @Nonnull SQLType type, @Nullable @NonNullableElements ReadOnlySet<SQLColumnConstraint> columnConstraints) {
        return new SQLColumnDeclaration(qualifiedColumnName, type, columnConstraints);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDeclaration> transcriber = new Transcriber<SQLColumnDeclaration>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDeclaration node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string) throws InternalException {
            dialect.transcribe(site, string, node.qualifiedColumnName);
            string.append(" ");
            dialect.transcribe(site, string, node.type);
            if (node.columnConstraint != null) {
                for (SQLColumnConstraint columnConstraint : node.columnConstraint) {
                    string.append(" ");
                    dialect.transcribe(site, string, columnConstraint);
                }
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLColumnDeclaration> getTranscriber() {
        return transcriber;
    }
    
}
