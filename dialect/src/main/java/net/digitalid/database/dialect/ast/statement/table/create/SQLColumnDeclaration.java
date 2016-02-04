package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.collections.readonly.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.reference.NonCapturable;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;

/**
 *
 */
public class SQLColumnDeclaration implements SQLNode<SQLColumnDeclaration> {
    
    public final @Nonnull SQLColumnName qualifiedColumnName;
    
    public final @Nonnull SQLType type;
    
    public final @Nullable @NonNullableElements ReadOnlyList<SQLColumnDefinition> columnDefinitions;
    
    public final @Nullable @NonNullableElements ReadOnlyList<SQLColumnConstraint> columnConstraints;
    
    private SQLColumnDeclaration(@Nonnull SQLColumnName qualifiedColumnName, @Nonnull SQLType type, @Nullable @NonNullableElements ReadOnlyList<SQLColumnDefinition> columnDefinitions, @Nullable @NonNullableElements ReadOnlyList<SQLColumnConstraint> columnConstraints) {
        this.qualifiedColumnName = qualifiedColumnName;
        this.type = type;
        this.columnDefinitions = columnDefinitions;
        this.columnConstraints = columnConstraints;
    }
    
    public static @Nonnull SQLColumnDeclaration of(@Nonnull SQLColumnName qualifiedColumnName, @Nonnull SQLType type, @Nullable @NonNullableElements ReadOnlyList<SQLColumnDefinition> columnDefinitions, @Nullable @NonNullableElements ReadOnlyList<SQLColumnConstraint> columnConstraints) {
        return new SQLColumnDeclaration(qualifiedColumnName, type, columnDefinitions, columnConstraints);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDeclaration> transcriber = new Transcriber<SQLColumnDeclaration>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDeclaration node, @Nonnull Site site, @Nonnull @NonCapturable StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.qualifiedColumnName, false);
            string.append(" ");
            dialect.transcribe(site, string, node.type, false);
            if (node.columnDefinitions != null) {
                for (SQLColumnDefinition columnDefinition : node.columnDefinitions) {
                    string.append(" ");
                    dialect.transcribe(site, string, columnDefinition, false);
                }
            }
            if (node.columnConstraints != null) {
                for (SQLColumnConstraint columnConstraint : node.columnConstraints) {
                    string.append(" ");
                    dialect.transcribe(site, string, columnConstraint, false);
                }
            }
        }
        
    };
    
    @Override
    public @Nonnull Transcriber<SQLColumnDeclaration> getTranscriber() {
        return transcriber;
    }
    
}
