package net.digitalid.database.dialect.ast.statement.table.create;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.collections.list.ReadOnlyList;
import net.digitalid.utility.exceptions.InternalException;
import net.digitalid.utility.immutable.ImmutableList;

import net.digitalid.database.core.table.Site;
import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.identifier.SQLColumnName;

/**
 * This SQL node represents a column declaration.
 */
public class SQLColumnDeclaration implements SQLNode<SQLColumnDeclaration> {
    
    /* -------------------------------------------------- Final Fields -------------------------------------------------- */
    
    /**
     * The column name of the column declaration.
     */
    public final @Nonnull SQLColumnName columnName;
    
    /**
     * The column type of the column declaration.
     */
    public final @Nonnull SQLType type;
    
    /**
     * The column definitions of the column declaration.
     */
    public final @Nullable ImmutableList<@Nonnull SQLColumnDefinition> columnDefinitions;
    
    /**
     * The column constraints of the column declaration.
     */
    public final @Nullable ImmutableList<@Nonnull SQLColumnConstraint> columnConstraints;
    
    /* -------------------------------------------------- Constructor -------------------------------------------------- */
    
    /**
     * Creates a new SQL column declaration with the given column name, sql type, column definitions and column constraints.
     */
    private SQLColumnDeclaration(@Nonnull SQLColumnName columnName, @Nonnull SQLType type, @Nullable ReadOnlyList<@Nonnull SQLColumnDefinition> columnDefinitions, @Nullable ReadOnlyList<@Nonnull SQLColumnConstraint> columnConstraints) {
        this.columnName = columnName;
        this.type = type;
        this.columnDefinitions = ImmutableList.with(columnDefinitions);
        this.columnConstraints = ImmutableList.with(columnConstraints);
    }
    
    /**
     * Returns an SQL column declaration with the given column name, sql type, column definitions and column constraints.
     */
    @Pure
    public static @Nonnull SQLColumnDeclaration of(@Nonnull SQLColumnName columnName, @Nonnull SQLType type, @Nullable ReadOnlyList<@Nonnull SQLColumnDefinition> columnDefinitions, @Nullable ReadOnlyList<@Nonnull SQLColumnConstraint> columnConstraints) {
        return new SQLColumnDeclaration(columnName, type, columnDefinitions, columnConstraints);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLColumnDeclaration> transcriber = new Transcriber<SQLColumnDeclaration>() {
        
        @Override
        protected @Nonnull String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLColumnDeclaration node, @Nonnull Site site) throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            string.append(dialect.transcribe(site, node.columnName));
            string.append(" ");
            string.append(dialect.transcribe(site, node.type));
            if (node.columnDefinitions != null) {
                for (SQLColumnDefinition columnDefinition : node.columnDefinitions) {
                    string.append(" ").append(dialect.transcribe(site, columnDefinition));
                }
            }
            if (node.columnConstraints != null) {
                for (SQLColumnConstraint columnConstraint : node.columnConstraints) {
                    string.append(" ").append(dialect.transcribe(site, columnConstraint));
                }
            }
            return string.toString();
        }
        
    };
    
    @Pure
    @Override
    public @Nonnull Transcriber<SQLColumnDeclaration> getTranscriber() {
        return transcriber;
    }
    
}
