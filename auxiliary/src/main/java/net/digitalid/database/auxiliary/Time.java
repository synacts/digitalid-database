package net.digitalid.database.auxiliary;

import java.text.DateFormat;
import java.util.Date;

import javax.annotation.Nonnull;

import net.digitalid.utility.annotations.method.Pure;
import net.digitalid.utility.annotations.ownership.NonCaptured;
import net.digitalid.utility.annotations.parameter.Modified;
import net.digitalid.utility.generator.annotations.generators.GenerateBuilder;
import net.digitalid.utility.generator.annotations.generators.GenerateConverter;
import net.digitalid.utility.generator.annotations.generators.GenerateSubclass;
import net.digitalid.utility.interfaces.LongNumerical;
import net.digitalid.utility.rootclass.RootClass;
import net.digitalid.utility.validation.annotations.generation.Default;
import net.digitalid.utility.validation.annotations.type.Immutable;

import net.digitalid.database.annotations.type.Embedded;

/**
 * This class models time in milliseconds for both dates and intervals.
 * Dates are calculated as milliseconds since 1 January 1970, 00:00:00 GMT.
 */
@Embedded
@Immutable
@GenerateBuilder
@GenerateSubclass
@GenerateConverter
public abstract class Time extends RootClass implements LongNumerical<Time> {
    
    /* -------------------------------------------------- Constants -------------------------------------------------- */
    
    /**
     * Stores the time of a decade (10 tropical years).
     */
    public static final @Nonnull Time DECADE = new TimeSubclass(315_569_251_900l);
    
    /**
     * Stores the time of two years (2 tropical years).
     */
    public static final @Nonnull Time TWO_YEARS = new TimeSubclass(63_113_850_380l);
    
    /**
     * Stores the time of a tropical year (default).
     */
    public static final @Nonnull Time TROPICAL_YEAR = new TimeSubclass(31_556_925_190l);
    
    /**
     * Stores the time of a calendar year (365 days).
     */
    public static final @Nonnull Time CALENDAR_YEAR = new TimeSubclass(31_536_000_000l);
    
    /**
     * Stores the time of a month (30 days).
     */
    public static final @Nonnull Time MONTH = new TimeSubclass(2_592_000_000l);
    
    /**
     * Stores the time of a week (7 days).
     */
    public static final @Nonnull Time WEEK = new TimeSubclass(604_800_000l);
    
    /**
     * Stores the time of a day (24 hours).
     */
    public static final @Nonnull Time DAY = new TimeSubclass(86_400_000l);
    
    /**
     * Stores the time of a half-day (12 hours).
     */
    public static final @Nonnull Time HALF_DAY = new TimeSubclass(43_200_000l);
    
    /**
     * Stores the time of an hour (60 minutes).
     */
    public static final @Nonnull Time HOUR = new TimeSubclass(3_600_000l);
    
    /**
     * Stores the time of a half-hour (30 minutes).
     */
    public static final @Nonnull Time HALF_HOUR = new TimeSubclass(1_800_000l);
    
    /**
     * Stores the time of a quarter-hour (15 minutes).
     */
    public static final @Nonnull Time QUARTER_HOUR = new TimeSubclass(900_000l);
    
    /**
     * Stores the time of a minute (60 seconds).
     */
    public static final @Nonnull Time MINUTE = new TimeSubclass(60_000l);
    
    /**
     * Stores the time of a second (1000 milliseconds).
     */
    public static final @Nonnull Time SECOND = new TimeSubclass(1_000l);
    
    /* -------------------------------------------------- Boundaries -------------------------------------------------- */
    
    /**
     * Stores the earliest possible time.
     */
    public static final @Nonnull Time MIN = new TimeSubclass(0l);
    
    /**
     * Stores the latest possible time.
     */
    public static final @Nonnull Time MAX = new TimeSubclass(Long.MAX_VALUE);
    
    /* -------------------------------------------------- Value -------------------------------------------------- */
    
    /**
     * Returns the value of this time in milliseconds.
     */
    @Pure
    @Override
    @Default(name = "CurrentTime", value = "System.currentTimeMillis()")
    public abstract long getValue();
    
    /* -------------------------------------------------- Arithmetic Operations -------------------------------------------------- */
    
    /**
     * Adds the given time to this time.
     */
    @Pure
    public @Nonnull Time add(@Nonnull Time time) {
        return new TimeSubclass(getValue() + time.getValue());
    }
    
    /**
     * Subtracts the given time from this time.
     */
    @Pure
    public @Nonnull Time subtract(@Nonnull Time time) {
        return new TimeSubclass(getValue() - time.getValue());
    }
    
    /**
     * Multiplies this time by the given factor.
     */
    @Pure
    public @Nonnull Time multiply(int factor) {
        return new TimeSubclass(getValue() * factor);
    }
    
    /**
     * Divides this time by the given divisor.
     */
    @Pure
    public @Nonnull Time divide(int divisor) {
        return new TimeSubclass(getValue() / divisor);
    }
    
    /**
     * Rounds this time to the given interval.
     */
    @Pure
    public @Nonnull Time round(@Nonnull Time interval) {
        return new TimeSubclass((getValue() + (getValue() > 0 ? 1 : -1) * interval.getValue() / 2) / interval.getValue() * interval.getValue());
    }
    
    /**
     * Rounds this time down to the given interval.
     */
    @Pure
    public @Nonnull Time roundDown(@Nonnull Time interval) {
        return new TimeSubclass(getValue() / interval.getValue() * interval.getValue());
    }
    
    /**
     * Returns whether this time is a multiple of the given interval.
     */
    @Pure
    public boolean isMultipleOf(@Nonnull Time interval) {
        return getValue() % interval.getValue() == 0;
    }
    
    /* -------------------------------------------------- Relative Time -------------------------------------------------- */
    
    /**
     * Returns this interval ago now.
     */
    @Pure
    public @Nonnull Time ago() {
        return new TimeSubclass(System.currentTimeMillis() - getValue());
    }
    
    /**
     * Returns this interval ahead of now.
     */
    @Pure
    public @Nonnull Time ahead() {
        return new TimeSubclass(System.currentTimeMillis() + getValue());
    }
    
    /* -------------------------------------------------- Conditions -------------------------------------------------- */
    
    /**
     * Returns whether this time lies in the future.
     */
    @Pure
    public boolean isInFuture() {
        return getValue() > System.currentTimeMillis();
    }
    
    /**
     * Returns whether this time lies in the past.
     */
    @Pure
    public boolean isInPast() {
        return getValue() < System.currentTimeMillis();
    }
    
    /* -------------------------------------------------- Retrievals -------------------------------------------------- */
    
    /**
     * Returns the number of calendar years in this time.
     */
    @Pure
    public long getYears() {
        return getValue() / CALENDAR_YEAR.getValue();
    }
    
    /**
     * Returns the number of months in this time (without the years).
     */
    @Pure
    public long getMonths() {
        return getValue() % CALENDAR_YEAR.getValue() / MONTH.getValue();
    }
    
    /**
     * Returns the number of weeks in this time (without the years and months).
     */
    @Pure
    public long getWeeks() {
        return getValue() % CALENDAR_YEAR.getValue() % MONTH.getValue() / WEEK.getValue();
    }
    
    /**
     * Returns the number of days in this time (without the years, months and weeks).
     */
    @Pure
    public long getDays() {
        return getValue() % CALENDAR_YEAR.getValue() % MONTH.getValue() % WEEK.getValue() / DAY.getValue();
    }
    
    /**
     * Returns the number of hours in this time (without the days).
     */
    @Pure
    public long getHours() {
        return getValue() % DAY.getValue() / HOUR.getValue();
    }
    
    /**
     * Returns the number of minutes in this time (without the hours).
     */
    @Pure
    public long getMinutes() {
        return getValue() % HOUR.getValue() / MINUTE.getValue();
    }
    
    /**
     * Returns the number of seconds in this time (without the minutes).
     */
    @Pure
    public long getSeconds() {
        return getValue() % MINUTE.getValue() / SECOND.getValue();
    }
    
    /**
     * Returns the number of milliseconds in this time (without the seconds).
     */
    @Pure
    public long getMilliseconds() {
        return getValue() % SECOND.getValue();
    }
    
    /* -------------------------------------------------- Formatting -------------------------------------------------- */
    
    private static final @Nonnull ThreadLocal<DateFormat> formatter = new ThreadLocal<DateFormat>() {
        @Override protected DateFormat initialValue() {
            return DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG);
        }
    };
    
    /**
     * Returns this time as a date with the given formatter.
     */
    @Pure
    public @Nonnull String asDate(@Nonnull DateFormat formatter) {
        return formatter.format(new Date(getValue()));
    }
    
    /**
     * Returns this time as a date.
     */
    @Pure
    public @Nonnull String asDate() {
        return asDate(formatter.get());
    }
    
    /**
     * Appends the given value with the given unit to the given string if the value is not zero.
     */
    @Pure
    private void append(@NonCaptured @Modified @Nonnull StringBuilder string, long value, @Nonnull String unit) {
        if (value != 0) {
            if (string.length() > 0) { string.append(", "); }
            string.append(value).append(" ").append(unit);
            if (value != 1 && value != -1) { string.append("s"); }
        }
    }
    
    /**
     * Returns this time as an interval.
     */
    @Pure
    public @Nonnull String asInterval() {
        final @Nonnull StringBuilder result = new StringBuilder();
        append(result, getYears(), "year");
        append(result, getMonths(), "month");
        append(result, getWeeks(), "week");
        append(result, getDays(), "day");
        append(result, getHours(), "hour");
        append(result, getMinutes(), "minute");
        append(result, getSeconds(), "second");
        append(result, getMilliseconds(), "millisecond");
        return result.toString();
    }
    
    /**
     * Returns this time as a date or an interval.
     */
    @Pure
    public @Nonnull String asString() {
        return isGreaterThan(DECADE) ? asDate() : asInterval();
    }
    
}
