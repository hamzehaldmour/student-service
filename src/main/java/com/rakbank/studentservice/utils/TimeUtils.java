package com.rakbank.studentservice.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Provides translations into java.time objects / methods from time based
 * objects found in java.util (and XmlGregorianCalendar)
 *
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public final class TimeUtils {

    private static final long NOT_DEFINED = -1;
    private static volatile long SYSTEM_STARTUP_TIME_MILLIS = -1;

    private TimeUtils() {
        throw new UnsupportedOperationException();
    }

    //// General -------------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Object temporal) {
        return toDate(temporal).getTime();
    }

    @Nonnull
    public static Date toDate(@Nonnull final Object temporal) {
        return toCalendar(temporal).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final Object temporal) {
        if (temporal instanceof Temporal) {
            if (temporal instanceof Clock) {
                return toCalendar((Clock) temporal);
            } else if (temporal instanceof Instant) {
                return toCalendar((Instant) temporal);
            } else if (temporal instanceof LocalDate) {
                return toCalendar((LocalDate) temporal);
            } else if (temporal instanceof LocalDateTime) {
                return toCalendar((LocalDateTime) temporal);
            } else if (temporal instanceof LocalTime) {
                return toCalendar((LocalTime) temporal);
            } else if (temporal instanceof OffsetDateTime) {
                return toCalendar((OffsetDateTime) temporal);
            } else if (temporal instanceof OffsetTime) {
                return toCalendar((OffsetTime) temporal);
            } else if (temporal instanceof ZonedDateTime) {
                return toCalendar((ZonedDateTime) temporal);
            } else {
                throw new IllegalArgumentException("Cases should be exhaustive, saw " + temporal);
            }
        } else if (temporal instanceof Number) {
            return toCalendar(new Date(((Number) temporal).longValue()));
        } else if (temporal instanceof Date) {
            return toCalendar((Date) temporal);
        } else if (temporal instanceof Calendar) {
            return (Calendar) temporal;
        } else if (temporal instanceof XMLGregorianCalendar) {
            return toCalendar((XMLGregorianCalendar) temporal);
        } else {
            throw new IllegalArgumentException("Cases should be exhaustive, saw " + temporal);
        }
    }

    public static long nanosToMillis(long nanos) {
        if (SYSTEM_STARTUP_TIME_MILLIS == -1) {
            synchronized (TimeUtils.class) {
                if (SYSTEM_STARTUP_TIME_MILLIS == -1) {
                    final long _nanos = System.nanoTime();
                    final long millis = System.currentTimeMillis();
                    final long millisSinceStartup = TimeUnit.NANOSECONDS.toMillis(_nanos);
                    SYSTEM_STARTUP_TIME_MILLIS = millis - millisSinceStartup;
                }
            }
        }

        return TimeUnit.NANOSECONDS.toMillis(nanos) + SYSTEM_STARTUP_TIME_MILLIS;
    }

    public static final Collector<Long, long[], Long> MIN_MILLIS_COMPARATOR = Collector.of(
            () -> new long[] { NOT_DEFINED },
            (arr, v) -> arr[0] = TimeUtils.minMillis(arr[0], v),
            (a, b) -> { a[0] = TimeUtils.minMillis(a[0], b[0]); return a; },
            arr -> arr[0]);

    public static long minMillis(long t1, long t2) {
        boolean undefined1;
        if ((undefined1 = t1 == -1) == (t2 == -1)) {
            return undefined1
                    ? -1 // both undefined
                    : Math.min(t1, t2); // neither is undefined
        } else {
            // one of the two is undefined, we always take the
            // defined value. In instances where -1 represents
            // 'forever', then we still take the defined value
            // as a minimum
            return undefined1 ? t2 : t1;
        }
    }

    public static final Collector<Long, long[], Long> MAX_MILLIS_COMPARATOR = Collector.of(
            () -> new long[] { NOT_DEFINED },
            (arr, v) -> arr[0] = TimeUtils.maxMillis(arr[0], v),
            (a, b) -> { a[0] = TimeUtils.maxMillis(a[0], b[0]); return a; },
            arr -> arr[0]);
    public static final Collector<Long, long[], Long> MAX_MILLIS_COMPARATOR2 = Collector.of(
            () -> new long[] { Long.MIN_VALUE },
            (arr, v) -> arr[0] = TimeUtils.maxMillis(arr[0], v, true),
            (a, b) -> { a[0] = TimeUtils.maxMillis(a[0], b[0], true); return a; },
            arr -> arr[0]);

    public static long maxMillis(long t1, long t2) {
        return maxMillis(t1, t2, false);
    }

    public static long maxMillis(long t1, long t2, boolean treatUndefinedAsMax) {
        boolean undefined1;
        if ((undefined1 = t1 == -1) == (t2 == -1)) {
            return undefined1
                    ? -1 // both undefined
                    : Math.max(t1, t2); // neither is undefined
        } else {
            // one of the two is undefined
            if (treatUndefinedAsMax) {
                return undefined1 ? t1 : t2;
            } else {
                // treat undefined as truly undefined
                return undefined1 ? t2 : t1;
            }
        }
    }

    //// java.util.Calendar --------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Calendar calendar) {
        return toMillis(toDate(calendar));
    }

    @Nonnull
    public static Date toDate(@Nonnull final Calendar calendar) {
        return calendar.getTime();
    }

    //// Clock ---------------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Clock clock) {
        return clock.millis();
    }

    @Nonnull
    public static Date toDate(@Nonnull final Clock clock) {
        return toCalendar(clock).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final Clock clock) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(clock.millis());
        cal.set(Calendar.ZONE_OFFSET, (int) TimeUnit.SECONDS.toMillis(ZoneOffset.of(clock.getZone().getId()).getTotalSeconds()));
        return cal;
    }

    //// java.util.Date ------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Date date) {
        return date.getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    //// DayOfWeek -----------------------------------------------------------------------------------------------------

    public static int toJavaCodeValue(@Nonnull final DayOfWeek dayOfWeek) {
        return dayOfWeek.getValue();
    }

    @Nonnull
    public static DayOfWeek getDayOfWeekFromJavaCodeValue(final int dowCode) {
        return DayOfWeek.of(dowCode);
    }

    @Nullable
    public static DayOfWeek getDayOfWeekFromString(@Nonnull final String dayOfWeek) {
        return EnumUtils.findMatching(Collections.singleton(dayOfWeek.trim().toLowerCase()),
                DayOfWeek.class,
                day -> Stream.of(day.name().toLowerCase())
                        .flatMap(n -> Stream.of(n,
                                n.substring(0, 3),
                                day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY || day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY
                                    ? n.substring(0, 2)
                                    : n.substring(0, 1)))
                        .collect(Collectors.toSet()),
                (a, b) -> !Collections.disjoint(a, b));
    }

    //// Duration ------------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Duration duration) {
        return duration.toMillis();
    }

    @Nonnull
    public static Duration toDuration(final long amount, @Nonnull final TimeUnit timeUnit) {
        return Duration.of(amount, convert(timeUnit));
    }

    @Nonnull
    public static Duration toDuration(@Nonnull final Date date) {
        return Duration.of(date.getTime(), ChronoUnit.MILLIS);
    }

    @Nonnull
    public static Duration toDuration(@Nonnull final Calendar cal) {
        return toDuration(cal.getTime());
    }

    private static final Triple<String, String, Long>[] UNITS = new Triple[] {
            Triple.of("d", "days", 86_400_000_000_000L),
            Triple.of("h", "hours", 3_600_000_000_000L),
            Triple.of("m", "minutes", 60_000_000_000L),
            Triple.of("s", "seconds", 1_000_000_000L),
            Triple.of("ms", "ms", 1_000_000L),
            Triple.of("μs", "μs", 1_000L),
            Triple.of("ns", "ns", 1L)
    };

    @Nonnull
    public static String createDurationString(final long duration) {
        return createDurationString(duration, TimeUnit.MILLISECONDS, true);
    }

    @Nonnull
    public static String createDurationString(final long duration,
                                              boolean longUnits) {
        return createDurationString(duration, TimeUnit.MILLISECONDS, longUnits);
    }

    @Nonnull
    public static String createDurationString(final long duration,
                                              @Nonnull final TimeUnit timeUnit) {
        return createDurationString(duration, timeUnit, true);
    }

    @Nonnull
    public static String createDurationString(final long duration,
                                              @Nonnull final TimeUnit timeUnit,
                                              boolean longUnits) {
        boolean negative = duration < 0;
        final long durationNs = timeUnit.toNanos(Math.abs(duration));
        final String dispFormat = (negative ? "-" : "") + "%.2f";

        for (int i=0; i<UNITS.length; i++) {
            String label = longUnits
                    ? UNITS[i].getMiddle()
                    : UNITS[i].getLeft();
            long factor = UNITS[i].getRight();

            long n = durationNs / factor;
            if (n > 0) {
                return String.format(dispFormat, (double) durationNs / (double) factor) + " " + label;
            }
        }

        return "0 ms";
    }

    //// Instant -------------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Instant instant) {
        return instant.toEpochMilli();
    }

    @Nonnull
    public static Date toDate(@Nonnull final Instant instant) {
        return new Date(instant.toEpochMilli());
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final Instant instant) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTimeInMillis(instant.toEpochMilli());
        return cal;
    }

    @Nonnull
    public static Instant toInstant(final long millis) {
        return Instant.ofEpochMilli(millis);
    }

    @Nonnull
    public static Instant toInstant(@Nonnull final Date date) {
        return date.toInstant();
    }

    @Nonnull
    public static Instant toInstant(@Nonnull final Calendar cal) {
        return cal.toInstant();
    }

    //// LocalDate -----------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final LocalDate localDate) {
        return TimeUnit.DAYS.toMillis(localDate.toEpochDay());
    }

    @Nonnull
    public static Date toDate(@Nonnull final LocalDate localDate) {
        return new Date(toMillis(localDate));
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final LocalDate localDate) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(localDate, cal);
        return cal;
    }

    private static void addToCalendar(@Nonnull final LocalDate localDate,
                                      @Nonnull final Calendar cal) {
        cal.set(Calendar.YEAR, localDate.getYear() + 1970);
        cal.set(Calendar.MONTH, localDate.getMonthValue());
        cal.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
    }

    @Nonnull
    public static LocalDate toLocalDate(final long millis) {
        return LocalDate.ofEpochDay(TimeUnit.MILLISECONDS.toDays(millis));
    }

    @Nonnull
    public static LocalDate toLocalDate(@Nonnull final Date date) {
        return toLocalDate(date.getTime());
    }

    @Nonnull
    public static LocalDate toLocalDate(@Nonnull final Calendar cal) {
        return toLocalDateTime(cal)
                .toLocalDate();
    }

    //// LocalDateTime -------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final LocalDateTime localDateTime) {
        return toMillis(localDateTime.atZone(ZoneId.systemDefault()));
    }

    @Nonnull
    public static Date toDate(@Nonnull final LocalDateTime localDateTime) {
        return toCalendar(localDateTime).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final LocalDateTime localDateTime) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(localDateTime.toLocalDate(), cal);
        addToCalendar(localDateTime.toLocalTime(), cal);
        return cal;
    }

    @Nonnull
    public static LocalDateTime toLocalDateTime(final long millis) {
        return toLocalDateTime(new Date(millis));
    }

    @Nonnull
    public static LocalDateTime toLocalDateTime(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toLocalDateTime(cal);
    }

    @Nonnull
    public static LocalDateTime toLocalDateTime(@Nonnull final Calendar cal) {
        return toOffsetDateTime(cal)
                .withOffsetSameInstant(ZoneOffset.of(ZoneId.systemDefault().getId()))
                .toLocalDateTime();
    }

    //// LocalTime -------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final LocalTime localTime) {
        return TimeUnit.SECONDS.toMillis(localTime.toSecondOfDay())
                + TimeUnit.NANOSECONDS.toMillis(localTime.getNano());
    }

    @Nonnull
    public static Date toDate(@Nonnull final LocalTime localTime) {
        return toCalendar(localTime).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final LocalTime localTime) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(localTime, cal);
        return cal;
    }

    private static void addToCalendar(@Nonnull final LocalTime localTime,
                                      @Nonnull final Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, localTime.getHour());
        cal.set(Calendar.MINUTE, localTime.getMinute());
        cal.set(Calendar.SECOND, localTime.getSecond());
        cal.set(Calendar.MILLISECOND, (int) TimeUnit.NANOSECONDS.toMillis(localTime.getNano()));
    }

    @Nonnull
    public static LocalTime toLocalTime(final long millis) {
        return toLocalTime(new Date(millis));
    }

    @Nonnull
    public static LocalTime toLocalTime(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toLocalTime(cal);
    }

    @Nonnull
    public static LocalTime toLocalTime(@Nonnull final Calendar cal) {
        return toLocalDateTime(cal)
                .toLocalTime();
    }

    //// Month ---------------------------------------------------------------------------------------------------------

    public static int toJavaCodeValue(@Nonnull final Month month) {
        return month.getValue();
    }

    @Nonnull
    public static Month getMonthFromJavaCodeValue(final int monthCode) {
        return Month.of(monthCode);
    }

    @Nullable
    public static Month getMonthFromString(@Nonnull final String month) {
        return EnumUtils.findMatching(Collections.singleton(month.trim().toLowerCase()),
                Month.class,
                m -> Stream.of(m.name().toLowerCase())
                        .flatMap(n -> Stream.of(n, n.substring(0, 3), m == Month.SEPTEMBER ? "sept" : null).filter(Objects::nonNull))
                        .collect(Collectors.toSet()),
                (a, b) -> !Collections.disjoint(a, b));
    }

    //// MonthDay ---------------------------------------------------------------------------------------------------------

    public static int toDayOfYear(@Nonnull final MonthDay monthDay) {
        return new Calendar.Builder()
                .set(Calendar.MONTH, monthDay.getMonthValue())
                .set(Calendar.DAY_OF_MONTH, monthDay.getDayOfMonth())
                .build()
                .get(Calendar.DAY_OF_YEAR);
    }

    //// OffsetDateTime -------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final OffsetDateTime offsetDateTime) {
        return toMillis(offsetDateTime.toInstant());
    }

    @Nonnull
    public static Date toDate(@Nonnull final OffsetDateTime offsetDateTime) {
        return toCalendar(offsetDateTime).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final OffsetDateTime offsetDateTime) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(offsetDateTime.toLocalDate(), cal);
        addToCalendar(offsetDateTime.toOffsetTime(), cal);
        return cal;
    }

    @Nonnull
    public static OffsetDateTime toOffsetDateTime(final long millis) {
        return OffsetDateTime.ofInstant(toInstant(millis), ZoneId.systemDefault());
    }

    @Nonnull
    public static OffsetDateTime toOffsetDateTime(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toOffsetDateTime(cal);
    }

    @Nonnull
    public static OffsetDateTime toOffsetDateTime(@Nonnull final Calendar cal) {
        return OffsetDateTime.of(
                cal.get(Calendar.YEAR) + 1970,
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                (int) TimeUnit.MILLISECONDS.toNanos(cal.get(Calendar.MILLISECOND)),
                ZoneOffset.ofTotalSeconds((int) TimeUnit.MILLISECONDS.toSeconds(cal.get(Calendar.ZONE_OFFSET))));
    }

    //// OffsetTime -------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final OffsetTime offsetTime) {
        return toMillis(offsetTime.withOffsetSameInstant(ZoneOffset.of(ZoneId.systemDefault().getId())).toLocalTime());
    }

    @Nonnull
    public static Date toDate(@Nonnull final OffsetTime offsetTime) {
        return toCalendar(offsetTime).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final OffsetTime offsetTime) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(offsetTime, cal);
        return cal;
    }

    private static void addToCalendar(@Nonnull final OffsetTime offsetTime,
                                      @Nonnull final Calendar cal) {
        addToCalendar(offsetTime.toLocalTime(), cal);
        cal.set(Calendar.ZONE_OFFSET, (int) TimeUnit.SECONDS.toMillis(offsetTime.getOffset().getTotalSeconds()));
    }

    @Nonnull
    public static OffsetTime toOffsetTime(final long millis) {
        return toOffsetTime(new Date(millis));
    }

    @Nonnull
    public static OffsetTime toOffsetTime(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toOffsetTime(cal);
    }

    @Nonnull
    public static OffsetTime toOffsetTime(@Nonnull final Calendar cal) {
        return toOffsetDateTime(cal)
                .toOffsetTime();
    }

    //// Period ------------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final Period period) {
        return TimeUnit.SECONDS.toMillis(period.getDays() * 86400
                + period.getMonths() * 2629746L
                + period.getYears() * 31556952L);
    }

    @Nonnull
    public static Period toPeriod(final long amount, @Nonnull final TimeUnit timeUnit) {
        long seconds = timeUnit.toSeconds(amount);
        int years = Long.valueOf(seconds / 31556952L).intValue();
        seconds -= (years * 31556952L);

        int months = Long.valueOf(seconds / 2629746L).intValue();
        seconds -= (years * 2629746L);

        int days = (int) TimeUnit.SECONDS.toDays(seconds);
        return Period.of(years, months, days);
    }

    //// TemporalUnit --------------------------------------------------------------------------------------------------

    @Nonnull
    public static TemporalUnit convert(@Nonnull final TimeUnit timeUnit) {
        switch (timeUnit) {
            case NANOSECONDS:  return ChronoUnit.NANOS;
            case MICROSECONDS: return ChronoUnit.MICROS;
            case MILLISECONDS: return ChronoUnit.MILLIS;
            case SECONDS:      return ChronoUnit.SECONDS;
            case MINUTES:      return ChronoUnit.MINUTES;
            case HOURS:        return ChronoUnit.HOURS;
            case DAYS:         return ChronoUnit.DAYS;
            default:
                throw new IllegalArgumentException("Switch statement should be exhaustive. Saw: " + timeUnit);
        }
    }

    @Nonnull
    public static Pair<TimeUnit, Long> convert(@Nonnull final TemporalUnit temporalUnit) {
        if (temporalUnit instanceof ChronoUnit) {
            switch ((ChronoUnit) temporalUnit) {
                case NANOS:   return Pair.of(TimeUnit.NANOSECONDS, 1L);
                case MICROS:  return Pair.of(TimeUnit.MICROSECONDS, 1L);
                case MILLIS:  return Pair.of(TimeUnit.MILLISECONDS, 1L);
                case SECONDS: return Pair.of(TimeUnit.SECONDS, 1L);
                case MINUTES: return Pair.of(TimeUnit.MINUTES, 1L);
                case HOURS:   return Pair.of(TimeUnit.HOURS, 1L);
                case DAYS:    return Pair.of(TimeUnit.DAYS, 1L);
                // anything below requires a conversion factor
                case HALF_DAYS:  return Pair.of(TimeUnit.HOURS, 12L);
                case WEEKS:      return Pair.of(TimeUnit.DAYS, 7L);
                case MONTHS:     return Pair.of(TimeUnit.SECONDS, 2629746L); // (seconds in year / 12), see ChronoUnit
                case YEARS:      return Pair.of(TimeUnit.SECONDS, 31556952L); // see ChronoUnit
                case DECADES:    return Pair.of(TimeUnit.SECONDS, 31556952L * 10L);
                case CENTURIES:  return Pair.of(TimeUnit.SECONDS, 31556952L * 100L);
                case MILLENNIA:  return Pair.of(TimeUnit.SECONDS, 31556952L * 1000L);
                case ERAS:       return Pair.of(TimeUnit.SECONDS, 31556952L * 1000_000_000L);
                case FOREVER:    return Pair.of(TimeUnit.DAYS, Long.MAX_VALUE);
                default:
                    throw new IllegalArgumentException("Switch statement should be exhaustive. Saw: " + temporalUnit);
            }
        } else {
            return Pair.of(TimeUnit.MILLISECONDS, temporalUnit.getDuration().toMillis());
        }
    }

    //// XMLGregorianCalendar ------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final XMLGregorianCalendar xmlGregorianCalendar) {
        return toMillis(xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime());
    }

    @Nonnull
    public static Date toDate(@Nonnull final XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar();
    }

    @Nonnull
    public static XMLGregorianCalendar toXMLGregorianCalendar(final long millis) {
        return toXMLGregorianCalendar(new Date(millis));
    }

    @Nonnull
    public static XMLGregorianCalendar toXMLGregorianCalendar(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toXMLGregorianCalendar(cal);
    }

    @Nonnull
    public static XMLGregorianCalendar toXMLGregorianCalendar(@Nonnull final Calendar cal) {
        try {
            // for some reason literatum has a proxy for the calendar, so (GregorianCalendar) GregorianCalendar.getInstance() fails!
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            cal.get(Calendar.SECOND),
                            cal.get(Calendar.MILLISECOND),
                            (cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)) / (60 * 1000));
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException(ex);
        }
    }

    //// Year ----------------------------------------------------------------------------------------------------------

    public static int toJavaYearValue(@Nonnull final Year year) {
        return year.getValue() - 1970;
    }

    @Nonnull
    public static Year getYearFromJavaYearValue(final int yearsSinceEpoch) {
        return Year.of(yearsSinceEpoch + 1970);
    }

    //// YearMonth -----------------------------------------------------------------------------------------------------


    //// ZonedDateTime -------------------------------------------------------------------------------------------------

    public static long toMillis(@Nonnull final ZonedDateTime zonedDateTime) {
        return toMillis(zonedDateTime.toInstant());
    }

    @Nonnull
    public static Date toDate(@Nonnull final ZonedDateTime zonedDateTime) {
        return toCalendar(zonedDateTime).getTime();
    }

    @Nonnull
    public static Calendar toCalendar(@Nonnull final ZonedDateTime zonedDateTime) {
        Calendar cal = GregorianCalendar.getInstance();
        addToCalendar(zonedDateTime.toLocalDate(), cal);
        addToCalendar(zonedDateTime.toOffsetDateTime().toOffsetTime(), cal);
        return cal;
    }

    @Nonnull
    public static ZonedDateTime toZonedDateTime(final long millis) {
        return ZonedDateTime.ofInstant(toInstant(millis), ZoneId.systemDefault());
    }

    @Nonnull
    public static ZonedDateTime toZonedDateTime(@Nonnull final Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(date);
        return toZonedDateTime(cal);
    }

    @Nonnull
    public static ZonedDateTime toZonedDateTime(@Nonnull final Calendar cal) {
        return ZonedDateTime.of(
                cal.get(Calendar.YEAR) + 1970,
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                (int) TimeUnit.MILLISECONDS.toNanos(cal.get(Calendar.MILLISECOND)),
                ZoneOffset.ofTotalSeconds((int) TimeUnit.MILLISECONDS.toSeconds(cal.get(Calendar.ZONE_OFFSET))));
    }

}
