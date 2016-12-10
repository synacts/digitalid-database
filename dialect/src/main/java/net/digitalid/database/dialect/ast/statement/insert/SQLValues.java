package net.digitalid.database.dialect.ast.statement.insert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.circumfixes.Brackets;
import net.digitalid.utility.collections.list.FreezableArrayList;
import net.digitalid.utility.exceptions.InternalException;

import net.digitalid.database.dialect.ast.SQLDialect;
import net.digitalid.database.dialect.ast.SQLParameterizableNode;
import net.digitalid.database.dialect.ast.Transcriber;
import net.digitalid.database.dialect.ast.expression.SQLExpression;
import net.digitalid.database.exceptions.operation.FailedSQLValueConversionException;
import net.digitalid.database.interfaces.SQLValueCollector;
import net.digitalid.database.subject.Site;

/**
 *
 */
public class SQLValues implements SQLParameterizableNode<SQLValues>, SQLValuesOrStatement<SQLValues> {
   
    public final @Nonnull FreezableArrayList<@Nonnull SQLExpression<?>> values;
    
    private SQLValues() {
        this.values = FreezableArrayList.withNoElements();
    }
    
    private SQLValues(@Nonnull FreezableArrayList<@Nonnull SQLExpression<?>> values) {
        this.values = values;
    }
    
    private SQLValues(@Nonnull SQLValues sqlValues) {
        this(sqlValues.values.clone());
    }
    
    @Pure
    public static @Nonnull SQLValues get() {
        return new SQLValues();
    }
    
    @Pure
    public static @Nonnull SQLValues get(@Nonnull FreezableArrayList<SQLExpression<?>> values) {
        return new SQLValues(values);
    }
    
    @Pure
    public static SQLValues clone(SQLValues c) {
        return new SQLValues(c);
    }
    
    @Pure
    @Override
    public void storeValues(@NonCaptured @Nonnull SQLValueCollector collector) throws FailedSQLValueConversionException {
        for (@Nullable SQLExpression sqlExpression : values) {
            // in the simplest case, the sqlExpression is a literal.
            sqlExpression.storeValues(collector);
        }
    }
    
    @Impure
    public void prependValue(@Nullable SQLExpression value) {
        values.add(0, value);
    }
    
    @Impure
    public void addValue(@Nullable SQLExpression value) {
        values.add(value);
    }
    
    /* -------------------------------------------------- SQL Node -------------------------------------------------- */
    
    /**
     * The transcriber that stores a string representation of this SQL node in the string builder.
     */
    private static final @Nonnull Transcriber<SQLValues> transcriber = new Transcriber<SQLValues>() {
        
        @Override
        protected String transcribe(@Nonnull SQLDialect dialect, @Nonnull SQLValues node, @Nonnull Site site)  throws InternalException {
            final @Nonnull StringBuilder string = new StringBuilder();
            if (node.values.size() > 0) {
                string.append("VALUES ");
                string.append(node.values.map(value -> dialect.transcribe(site, value)).join(Brackets.ROUND, ", "));
            }
            return string.toString();
        }
        
    };
 
    @Pure
    @Override
    public @Nonnull Transcriber<SQLValues> getTranscriber() {
        return transcriber;
    }
    
}
