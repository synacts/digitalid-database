package net.digitalid.database.dialect.statement.select.ordered;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.immutable.ImmutableList;
import net.digitalid.utility.storage.interfaces.Unit;
import net.digitalid.utility.validation.annotations.elements.NonNullableElements;
import net.digitalid.utility.validation.annotations.size.NonEmpty;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.sql.SQLFraction;
import net.digitalid.database.dialect.SQLDialect;
import net.digitalid.database.dialect.statement.select.SQLSelectStatement;
import net.digitalid.database.dialect.statement.select.unordered.SQLUnorderedSelectStatement;

/**
 * An SQL select statement with an order or limit clause.
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public interface SQLOrderedSelectStatement extends SQLSelectStatement {
    
    /* -------------------------------------------------- Select Statement -------------------------------------------------- */
    
    /**
     * Returns the unordered select statement on which this ordered select statement is based.
     */
    @Pure
    public @Nonnull SQLUnorderedSelectStatement getSelectStatement();
    
    /* -------------------------------------------------- Orders -------------------------------------------------- */
    
    /**
     * Returns the optional orders of this ordered select statement.
     */
    @Pure
    public @Nullable @NonNullableElements @NonEmpty ImmutableList<? extends SQLOrderingTerm> getOrders();
    
    /* -------------------------------------------------- Limit -------------------------------------------------- */
    
    /**
     * Returns the optional limit of this ordered select statement.
     */
    @Pure
    public @Nullable Integer getLimit();
    
    /* -------------------------------------------------- Offset -------------------------------------------------- */
    
    /**
     * Returns the optional offset of this ordered select statement.
     */
    @Pure
    public @Nullable Integer getOffset();
    
    /* -------------------------------------------------- Unparse -------------------------------------------------- */
    
    @Pure
    @Override
    public default void unparse(@Nonnull SQLDialect dialect, @Nonnull Unit unit, @NonCaptured @Modified @Nonnull @SQLFraction StringBuilder string) {
        dialect.unparse(getSelectStatement(), unit, string);
        final @Nullable @NonNullableElements @NonEmpty ImmutableList<? extends SQLOrderingTerm> orders = getOrders();
        if (orders != null) {
            string.append(" ORDER BY ");
            dialect.unparse(orders, unit, string);
        }
        final @Nullable Integer limit = getLimit();
        final @Nullable Integer offset = getOffset();
        if (limit != null || offset != null) {
            string.append(" LIMIT ").append(limit != null ? limit : Integer.MAX_VALUE);
            if (offset != null) { string.append(" OFFSET ").append(offset); }
        }
    }
    
}
