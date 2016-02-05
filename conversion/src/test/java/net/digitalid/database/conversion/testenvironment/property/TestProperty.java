package net.digitalid.database.conversion.testenvironment.property;

import javax.annotation.Nonnull;

import net.digitalid.utility.property.Validated;
import net.digitalid.utility.property.ValueValidator;
import net.digitalid.utility.property.nonnullable.WritableNonNullableProperty;

/**
 *
 */
public class TestProperty extends WritableNonNullableProperty<Boolean> {
    
    private Boolean flag;
    
    private TestProperty() {
        super(ValueValidator.DEFAULT);
        set(true);
    } 
    
    public static @Nonnull TestProperty getProperty() {
        return new TestProperty();
    }
    
    @Override
    public void set(@Nonnull @Validated Boolean newValue) {
        this.flag = newValue;
    }
    
    @Override
    public @Nonnull Boolean get() {
        return flag;
    }
    
}
