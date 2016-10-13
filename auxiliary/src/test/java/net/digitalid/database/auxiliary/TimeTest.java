package net.digitalid.database.auxiliary;

import javax.annotation.Nonnull;

import net.digitalid.utility.time.Time;
import net.digitalid.utility.time.TimeBuilder;

import org.junit.Assert;
import org.junit.Test;

public class TimeTest {
    
    @Test
    public void testSomeMethod() {
        final long value = 1_413_423;
        final @Nonnull Time time = TimeBuilder.withValue(value).build();
        Assert.assertEquals(value, time.getValue());
    }
    
}
