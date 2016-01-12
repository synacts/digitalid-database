package net.digitalid.database.core.converter.sql;

import javax.annotation.Nonnull;
import net.digitalid.database.core.converter.sql.serializer.SQLBooleanConverter;
import net.digitalid.database.core.converter.sql.serializer.SQLConvertibleConverter;
import net.digitalid.utility.conversion.Convertible;
import net.digitalid.utility.conversion.Format;
import net.digitalid.utility.conversion.TypeMapper;

public class SQLFormat extends Format<SQLConverter> {

    /**
     * Creates a new converter which converts boolean to SQL.
     */
    private final static @Nonnull SQLConverter<Boolean> BOOLEAN_CONVERTER = new SQLBooleanConverter();

    /**
     * Creates a new converter which converts convertible objects to SQL.
     */
    final static @Nonnull SQLConverter<? extends Convertible> CONVERTIBLE_CONVERTER = new SQLConvertibleConverter();
    
    @Override
    protected @Nonnull SQLConverter getBooleanConverter() {
        return BOOLEAN_CONVERTER;
    }
    
    @Override
    protected @Nonnull SQLConverter getInteger08Converter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getInteger16Converter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getInteger32Converter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getInteger64Converter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getIntegerConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getCharacterConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getStringConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getBinaryConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getConvertibleConverter() {
        return CONVERTIBLE_CONVERTER;
    }
    
    @Override
    protected @Nonnull SQLConverter getCollectionConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getArrayConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getMapConverter() {
        return null;
    }
    
    @Override
    protected @Nonnull SQLConverter getTypeConverter(@Nonnull TypeMapper<?, ?> typeMapper) {
        return null;
    }
    
}
