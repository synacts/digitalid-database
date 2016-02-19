package net.digitalid.database.conversion;

import java.util.Collection;

import javax.annotation.Nonnull;

import net.digitalid.utility.generator.conversion.Convertible;
import net.digitalid.utility.conversion.converter.Format;
import net.digitalid.utility.conversion.converter.TypeMapper;
import net.digitalid.utility.property.ReadOnlyProperty;

import net.digitalid.database.conversion.converter.SQLConverter;
import net.digitalid.database.conversion.converter.primitive.bool.SQLBooleanConverter;
import net.digitalid.database.conversion.converter.object.SQLObjectConverter;
import net.digitalid.database.conversion.converter.primitive.integer.SQLInteger32Converter;
import net.digitalid.database.conversion.converter.iterable.SQLArrayConverter;
import net.digitalid.database.conversion.converter.iterable.SQLCollectionsConverter;
import net.digitalid.database.conversion.converter.property.SQLPropertyConverter;

public class SQLFormat extends Format<SQLConverter> {
    
    @SuppressWarnings("unchecked")
    public <T> SQLConverter<T> getSQLConverter(Class<T> type) {
        return getConverter(type);
    }
    
    /**
     * Creates a new converter which converts boolean to SQL.
     */
    private final static @Nonnull SQLConverter<Boolean> BOOLEAN_CONVERTER = new SQLBooleanConverter();
    
    private final static @Nonnull SQLConverter<Integer> INTEGER32_CONVERTER = new SQLInteger32Converter();
    
    private final static @Nonnull SQLConverter<ReadOnlyProperty<?, ?>> PROPERTY_CONVERTER = new SQLPropertyConverter();
    
    private final static @Nonnull SQLConverter<Collection<?>> COLLECTION_CONVERTER = new SQLCollectionsConverter();
    
    private final static @Nonnull SQLConverter<Object[]> ARRAY_CONVERTER = new SQLArrayConverter();
    
    /**
     * Creates a new converter which converts convertible objects to SQL.
     */
    final static @Nonnull SQLConverter<? extends Convertible> CONVERTIBLE_CONVERTER = new SQLObjectConverter<>();
    
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
        return INTEGER32_CONVERTER;
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
    protected @Nonnull SQLConverter getPropertyConverter() {
        return PROPERTY_CONVERTER;
    }
    
    @Override
    protected @Nonnull SQLConverter getCollectionConverter() {
        return COLLECTION_CONVERTER;
    }
    
    @Override
    protected @Nonnull SQLConverter getArrayConverter() {
        return ARRAY_CONVERTER;
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
