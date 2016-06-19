package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;

/**
 *
 */
public class SQLColumnDeclaration implements SQLNode<SQLColumnDeclaration> {
    
    public final @Nonnull SQLColumnName columnName;
    
    public final @Nonnull SQLType type;
    
    public final @Nullable ReadOnlyList<@Nonnull SQLColumnDefinition> columnDefinitions;
    
    public final @Nullable ReadOnlyList<@Nonnull SQLColumnConstraint> columnConstraints;
    
    private SQLColumnDeclaration(@Nonnull SQLColumnName columnName, @Nonnull SQLType type, @Nullable ReadOnlyList<@Nonnull SQLColumnDefinition> columnDefinitions, @Nullable ReadOnlyList<@Nonnull SQLColumnConstraint> columnConstraints) {
        this.columnName = columnName;
        this.type = type;
        this.columnDefinitions = columnDefinitions;
        this.columnConstraints = columnConstraints;
    }
    
    public static @Nonnull SQLColumnDeclaration of(@Nonnull SQLColumnName columnName, @Nonnull SQLType type, @Nullable ReadOnlyList<@Nonnull SQLColumnDefinition> columnDefinitions, @Nullable ReadOnlyList<@Nonnull SQLColumnConstraint> columnConstraints) {
        return new SQLColumnDeclaration(columnName, type, columnDefinitions, columnConstraints);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDeclaration> transcriber = new Transcriber<SQLColumnDeclaration>() {
        
        @Override
        protected void transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDeclaration node, @Nonnull Site site, @Nonnull @NonCaptured StringBuilder string, boolean parameterizable) throws InternalException {
            dialect.transcribe(site, string, node.columnName, false);
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
