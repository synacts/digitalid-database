package net.digitalid.database.conversion.utility;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.generics.Specifiable;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.conversion.interfaces.Converter;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.exceptions.DatabaseException;
import net.digitalid.database.interfaces.encoder.SQLQueryEncoder;

/**
 *
 */
@Immutable
@GenerateBuilder
@GenerateSubclass
public abstract class WhereCondition<@Specifiable WHERE_TYPE> {
    
    @Pure
    public abstract Converter<WHERE_TYPE, ?> getWhereConverter();
    
    @Pure
    public abstract WHERE_TYPE getWhereObject();
    
    /**
     * Indicates on which field the where clause should match.
     */
    @Pure
    @Default("\"\"")
    public abstract @Nonnull String getWherePrefix();
    
    @Pure
    public void encode(@NonCaptured @Modified @Nonnull SQLQueryEncoder queryEncoder) throws DatabaseException {
        queryEncoder.encodeNullableObject(getWhereConverter(), getWhereObject());
    }
    
}
