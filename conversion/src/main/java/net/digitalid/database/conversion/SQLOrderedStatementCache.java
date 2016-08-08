package net.digitalid.database.conversion;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.conversion.converter.Converter;
import net.digitalid.utility.conversion.converter.CustomField;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.generator.annotations.interceptors.Cached;

import net.digitalid.database.conversion.columndeclarations.SQLColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLInsertIntoTableColumnDeclarations;
import net.digitalid.database.conversion.columndeclarations.SQLOrderedStatements;
import net.digitalid.database.conversion.columndeclarations.SQLSelectFromTableColumnDeclarations;
import net.digitalid.database.dialect.ast.statement.insert.SQLInsertStatement;
import net.digitalid.database.dialect.ast.statement.select.SQLSelectStatement;

/**
 *
 */
@GenerateSubclass
public abstract class SQLOrderedStatementCache {
    
    public static SQLOrderedStatementCache INSTANCE = new SQLOrderedStatementCacheSubclass();
    
    @Pure
    private <S, CD extends SQLColumnDeclarations<CD, ?, S>> @Nonnull SQLOrderedStatements<S, CD> getOrderedStatements(@Nonnull Converter<?, ?> converter, @Nonnull SQLColumnDeclarations<CD, ?, S> insertDeclaration) {
        for (@Nonnull CustomField field : converter.getFields()) {
            insertDeclaration.setField(field);
        }
        return insertDeclaration.getOrderedStatements();
    }
    
    @Pure
    @Cached
    public @Nonnull SQLOrderedStatements<@Nonnull SQLInsertStatement, @Nonnull SQLInsertIntoTableColumnDeclarations> getOrderedInsertStatements(@Nonnull Converter<?, ?> converter) {
        final @Nonnull String tableName = converter.getName();
        
        final @Nonnull SQLInsertIntoTableColumnDeclarations insertDeclaration = SQLInsertIntoTableColumnDeclarations.get(tableName);
        return getOrderedStatements(converter, insertDeclaration);
    }
    
    @Pure
    @Cached
    public @Nonnull SQLOrderedStatements<@Nonnull SQLSelectStatement, @Nonnull SQLSelectFromTableColumnDeclarations> getOrderedSelectStatements(@Nonnull Converter<?, ?> converter) {
        final @Nonnull String tableName = converter.getName();
    
        final @Nonnull SQLSelectFromTableColumnDeclarations selectDeclaration = SQLSelectFromTableColumnDeclarations.get(tableName);
        return getOrderedStatements(converter, selectDeclaration);
    }
    
}
