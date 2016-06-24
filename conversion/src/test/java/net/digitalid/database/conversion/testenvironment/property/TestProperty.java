package net.digitalid.database.conversion.testenvironment.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.digitalid.utility.annotations.method.Impure;
import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.Capturable;
import net.digitalid.utility.functional.interfaces.Predicate;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.property.nonnullable.WritableNonNullableProperty;
import net.digitalid.utility.validation.annotations.value.Valid;

/**
 *
 */
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
public abstract class TestProperty extends WritableNonNullableProperty<Boolean> {
    
    private Boolean flag;
    
    TestProperty() {
        set(true);
    } 
    
    @Impure
    @Override
    public @Capturable @Nullable @Valid Boolean set(@Nonnull @Valid Boolean newValue) {
        Boolean oldValue = flag;
        this.flag = newValue;
        return oldValue;
    }
    
    @Pure
    @Override
    public @Nonnull Boolean get() {
        return flag;
    }
    
    @Pure
    @Override
    public @Nonnull Predicate<? super Boolean> getValueValidator() {
        return (any) -> true;
    }
    
}
