package com.rakbank.studentservice.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class DateUtils {

    /**
     * now plus 10
     */
    private static final int MAX_VALID_YEAR = getYear(new Date()) + 10;

    /**
     * all possible date formats as defined in class {@link SimpleDateFormat}
     */
    public static final String FORMAT_TIMESTAMP_DEFAULT = "yyyyMMddHHmmssSSS";

    public static final String[] MONTHS = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    public static final String[] LOWER_MONTHS = {
            "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec"
    };
    public static final char[] DELIMITERS = {
            '.',
            '-',
            '/',
            ',',
            ':',
            ' '};
    /**
     * variations of date formats found in coverDate fields. Month-Year (MY) formats
     * NOTE: the long patterns have to come before their similarly looking short patterns.
     * E.g. MM-dd-yyyy(1) should come before dd-yyyy(2) because otherwise (2) will always be matched.
     * Not allowed to contain patterns without delimiters, see bug 4764 c19
     */
    public static final String[] DATE_FORMAT_MY = {
            "MM dd yyyy",
            "MMM dd yyyy",
            "dd MMM yyyy",
            "MMM yyyy",
            "MM yyyy"
    };
    /**
     * variations of date formats found in coverDate fields. Year-Month (YM) formats
     * NOTE: the long patterns have to come before their similarly looking short patterns.
     * E.g. yyyy-MM-dd(1) should come before yyyy-dd(2) because otherwise (2) will always be matched.
     * Not allowed to contain patterns without delimiters, see bug 4764 c19
     */
    public static final String[] DATE_FORMAT_YM = {
            "yyyy MM dd",
            "yyyy MMM dd",
            "yyyy MMM",
            "yyyy MM"
    };

    /**
     * attempts to parse a cover srcDate, using all variations found in
     * <tt>DATE_FORMAT_MY[]</tt> and
     * <tt>DATE_FORMAT_YM[]</tt>
     *
     * @param srcDate source date to be handled.
     * @return parsed srcDate, never <tt>null</tt>
     * @throws NumberFormatException    in case <tt>srcDate</tt> is in an innapropriate, ambiguous format that doesn't allow
     *                                  safe parsing. Eg 121911 can be interpreted both as 12-1911 adn 1219-11.
     * @throws IllegalStateException    in case the parsed srcDate represents a year before 1900 or after 2010. It acts as a
     *                                  sanity check in case we parse <tt>srcDate</tt> wrong.
     * @throws IllegalArgumentException in case srcDate==null, OR srcDate.length() < 4. <tt>srcDate</tt> should at l\east describe a 4-digit year
     */
    public static Date parseDate(String srcDate) {
        return parseDate(srcDate, true);
    }

    /**
     * attempts to parse a cover srcDate, using all variations found in <tt>DateUtil.dateFormat[]</tt>
     *
     * @param srcDate source date string to be handled.
     * @return parsed srcDate, never <tt>null</tt>
     * @throws NumberFormatException    in case <tt>srcDate</tt> is in an innapropriate, ambiguous format that doesn't allow
     *                                  safe parsing. Eg 121911 can be interpreted both as 12-1911 adn 1219-11.
     * @throws IllegalStateException    in case the parsed srcDate represents a year before 1900 or after 2010. It acts as a
     *                                  sanity check in case we parse <tt>srcDate</tt> wrong.
     * @throws IllegalArgumentException in case srcDate==null, OR srcDate.length() < 4. <tt>srcDate</tt> should at least describe a 4-digit year
     */
    @Nullable
    public static Date parseDate(@Nullable String srcDate, boolean validate) {
        if (srcDate == null
                || (srcDate = srcDate.trim()).isEmpty()) {
            return null;
        }

        String normalizedDate = normalizeStringDate(srcDate);
        final int normalizedLength = normalizedDate.length();

        String[] dateFormats;
        if (StringUtils.isInteger(normalizedDate)) {
            switch (normalizedLength) {
                case 4:
                    dateFormats = new String[]{"yyyy"};
                    break;

                case 5:
                    dateFormats = new String[]{endsInYear(srcDate) ? "Myyyy" : "yyyyM"};
                    break;

                case 6:
                    dateFormats = new String[]{endsInYear(srcDate) ? "MMyyyy" : "yyyyMM"};
                    break;

                case 8:
                    dateFormats = new String[]{"yyyyMMdd"};
                    break;

                case 12:
                    dateFormats = new String[]{"yyyyMMddHHmm"};
                    break;

                case 14:
                    dateFormats = new String[]{"yyyyMMddHHmmss"};
                    break;

                case 17:
                    dateFormats = new String[]{FORMAT_TIMESTAMP_DEFAULT};
                    break;

                default:
                    throw new IllegalArgumentException("dates with no delimiters (all-numeric) should be either"
                            + " 4, 5, 6, 8, 12, 14 or 17 digits long. Found: " + srcDate);
            }
        } else {
            // delimitted, either YM or MY
            if (endsInYear(normalizedDate)) {
                dateFormats = DATE_FORMAT_MY;
            } else {
                dateFormats = DATE_FORMAT_YM;
            }
        }

        // loops over each dateFormats, stops as soon as a valid srcDate string is parsed
        Date parsedDate = null;
        for (int i = 0; i < dateFormats.length && null == parsedDate; i++) {
            String format = dateFormats[i];

            // Check if time is also included in the input text date.
            if (normalizedDate.length() > "YYYY-MM-DD".length() &&
                    normalizedDate.contains(":") &&
                    normalizedDate.indexOf(":", normalizedDate.indexOf(":") + 1) != -1) {
                format = format + " hh:mm:ss";
            }
            parsedDate = parseDateString(normalizedDate, format);
        }

        if (null == parsedDate) {
            throw new NumberFormatException("Failed to parse '" + srcDate + "' as a srcDate; Ambiguous/Unknown format.");
        } else if (validate) {
            // sanity check. We know cover dates cannot have 'any' value.
            if (!isLegalYearRange(getYear(parsedDate))) {
                throw new NumberFormatException("Input '" + srcDate + "' failed to parse as a legal srcDate; " +
                        "Parsed as: " + parsedDate);
            }
        }

        return parsedDate;
    }

    /**
     * ensures date is non-empty, of minimum length, and normalizes dates with a single letter char (a1998, 1998a)
     *
     * @param date date input.
     * @return normalized string date.
     */
    @Nonnull
    private static String normalizeStringDate(@Nonnull String date) {
        if (date == null || (date = date.trim()).isEmpty()) {
            throw new IllegalArgumentException("Date may not be null or empty");
        }

        final int dateLength = date.length();
        if (dateLength < 4) {
            // should at least describe the year, as in '1999'.
            throw new IllegalArgumentException("Date '" + date + "' should at least describe a 4-digit year");
        }

        return date;
    }

    /**
     * parses a date string.
     * this method will replace all delimiters from
     *
     * @param input  date in fomat <tt>yyyy-MM</tt> e.g. <tt>2001-09</tt>.
     * @param format all possible date formats as defined in <code>SimpleDateFormat</code> class
     * @return parsed date, or null if format is not valid.
     */
    @Nullable
    public static Date parseDateString(String input, String format) {
        if (input == null) return null;

        input = normalizePattern(input);
        format = normalizePattern(format);

        if (StringUtils.isNullOrEmptyOrWhitespace(input) ||
                StringUtils.isNullOrEmptyOrWhitespace(format)) {
            return null;
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
            Date result = formatter.parse(input);

            Calendar cal = Calendar.getInstance();
            cal.setTime(result);

            // for some reason, java parses
            //  new SimpleDateFormat("MM/yyyy").parse("2006/1")
            // returns Feb 01, 168.  we only accept 4-digit years
            if (cal.get(Calendar.YEAR) >= 1000) {
                return result;
            }
        } catch (ParseException e) {
            // fine
        }

        return null;
    }

    private static String normalizePattern(String pattern) {
        if (StringUtils.isNullOrEmptyOrWhitespace(pattern)) {
            return null;
        }

        String input = pattern;
        for (char delim : DELIMITERS) {
            input = input.replace(delim, ' ');
        }
        input = input.replaceAll(" +", " ");
        return input.trim();
    }

    /**
     * @param date date.
     * @return shorter format of date if in the same year or day
     */
    public static String conciseFormat(Date date) {
        String format;
        Date now = new Date();
        if (date.getYear() != now.getYear())
            format = "MMM/dd/yyyy HH:mm";
        else if (date.getMonth() != now.getMonth() || date.getDate() != now.getDate())
            format = "MMM/dd HH:mm";
        else
            format = "HH:mm";
        return new SimpleDateFormat(format).format(date);
    }

    public static int monthToInt(String monthMMM) {
        if (monthMMM == null)
            return -1;

        monthMMM = monthMMM.toLowerCase();
        for (int i = 0; i < MONTHS.length; i++) {
            if (LOWER_MONTHS[i].equals(monthMMM))
                return i;
        }

        return -1;
    }

    /**
     * @param    dateTime    Date object, with timestamp.
     * @return new date object storing date (year, month, date) only.
     */
    @Nullable
    public static Date getDateOnly(@Nullable Date dateTime) {
        if (dateTime == null) {
            return null;
        }
        return createDate(getYear(dateTime), getMonth(dateTime) + 1, getDate(dateTime));
    }

    /**
     * @param date Date to be handled.
     * @return full year (4 digit: yyyy) of given date.
     * i.e., date.getYear() + 1900
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.YEAR);
    }

    /**
     * @param date date to be handled.
     * @return month of given date, <u>zero-based</u> (0-11)
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.MONTH);
    }

    /**
     * @param date date to be handled.
     * @return date of given date. i.e. day of the month
     */
    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param date date to be handled
     * @param days number of days to be added to the date
     * @return date after specified number of days
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date addMonths(Date date, int numMonths) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MONTH, numMonths);
        return cal.getTime();
    }

    /**
     * Return a new Date instance of given parameters.
     * <p>
     * Date/time interpretation is to be lenient, see
     * {@link DateUtils#createDate(int, int, int, boolean)} for more information
     * regarding leniency.
     *
     * @param    year year (<b>not</b> since 1900).
     * @param    month month (1-12).
     * @param    day    date (1-31).
     * @return Returns a new date of given parameters.
     */
    public static Date createDate(int year, int month, int day) {
        return createDate(year, month, day, true);
    }

    /**
     * Return a new Date instance of given parameters.
     *
     * @throws IllegalArgumentException if interpretation is non-lenient and any of the calendar fields
     *                                  have invalid values.
     * @param    year year (<b>not</b> since 1900).
     * @param    month month (1-12).
     * @param    day    date (1-31).
     * @param    lenient    Specifies whether or not date/time interpretation is to
     * be lenient. With lenient interpretation, a date such as
     * "February 942, 1996" will be treated as being equivalent
     * to the 941st day after February 1, 1996. With strict
     * (non-lenient) interpretation, such dates will cause an
     * exception to be thrown.
     * @return Returns a new date of given parameters.
     */
    public static Date createDate(int year, int month, int day, boolean lenient) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setLenient(lenient);
        //noinspection MagicConstant
        cal.set(year, month - 1, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Return a new Date instance of given parameters.
     *
     * @param year   year (<b>not</b> since 1900).
     * @param month  month (1-12).
     * @param day    date (1-31).
     * @param hour   hour (0-23).
     * @param minute minute (0-59).
     * @param second second (0-59).
     * @return new date of given parameters.
     */
    public static Date createDate(int year, int month, int day,
                                  int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        //noinspection MagicConstant
        cal.set(year, month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * determines whether <tt>date</tt> ends with a year. I.e. if its format is month-year
     * or year-month, to apply the relevant dateFormat patterns.
     *
     * @param date date string to be handled.
     * @return false if it doesn't end in year (e.g. ends in month or day - yyyyMMdd).
     * true even if it's just the year (yyyy).
     * @throws NumberFormatException    in case <tt>date</tt> is in an innapropriate, ambiguous format that doesn't allow
     *                                  safe parsing. Eg 121911 can be interpreted both as 12-1911 adn 1219-11.
     * @throws IllegalArgumentException in case <tt>date</tt> is less than 4 chars (can't represent a year)
     */
    private static boolean endsInYear(String date) throws NumberFormatException, IllegalArgumentException {
        final int dateLength = date.length();
        if (dateLength < 4)
            throw new IllegalArgumentException("Date \"" + date + "\" should at least describe a 4-digit year");

        boolean endsInYear = false;
        String fourLastChars = date.substring(dateLength - 4);
        if (StringUtils.isInteger(fourLastChars)) {
            // a fifth consecutive digit can be ambiguous, so we must check.
            if (dateLength > 4 && Character.isDigit(date.charAt(dateLength - 5))) {
                // the fifth and sixth consecutive digits are allowed to only refer to the month.
                final int monthOffset;
                if (dateLength > 5 && Character.isDigit(date.charAt(dateLength - 6)))
                    monthOffset = 2;    // 121996
                else
                    monthOffset = 1;    // 71996

                final int yearOffset = 4;
                // month-year combinations
                final int m_y_Year = Integer.parseInt(date.substring(dateLength - yearOffset, dateLength));
                final int m_y_Month = Integer.parseInt(date.substring(dateLength - (yearOffset + monthOffset), dateLength - yearOffset));
                // year-month combinations
                final int y_m_Year = Integer.parseInt(date.substring(0, yearOffset));
                final int y_m_Month = Integer.parseInt(date.substring(yearOffset, yearOffset + monthOffset));

                if (isLegalMonthRange(m_y_Month) && isLegalYearRange(m_y_Year)) {
                    endsInYear = true;
                    // MMyyyy fails to parse "12002", but Myyyy handles it fine!

                    // sanityCheck - if M-Y is valid, Y-M cannot be valid at the same time
                    if (isLegalMonthRange(y_m_Month) && isLegalYearRange(y_m_Year))
                        throw new NumberFormatException("Failed to parse a valid date from \"" + date + "\" - It probably " +
                                "represents an invalid date range. Separating 'year' from 'month' " +
                                "with a delimiter (space, comma, hyphen, fwd-slash) usually helps");
                } else {
                    // presumably its Year-Month
                    endsInYear = false;

                    // verifies - if any of the two are not true, we have a problem
                    if (!isLegalMonthRange(y_m_Month) || !isLegalYearRange(y_m_Year))
                        throw new NumberFormatException("Failed to parse a valid date from \"" + date + "\" - It probably " +
                                "represents an invalid date range. Separating 'year' from 'month' " +
                                "with a delimiter (space, comma, hyphen, fwd-slash) usually helps");
                }
            } else {
                // the date string either consists of exactly 4 digits or the 5th char is not a digit
                endsInYear = true;
            }
        }

        return endsInYear;
    }

    private static boolean isLegalYearRange(final int year) {
        return year > 1500 && year < MAX_VALID_YEAR;
    }

    private static boolean isLegalMonthRange(final int month) {
        return month >= 1 && month <= 12;
    }

    /**
     * determines whether {@param date1} and {@param date2} have the same date(year, month, day)
     *
     * @param date1 Date object contains timestamp or date.
     * @param date2 Date object contains timestamp or date.
     * @return true, in case date1 and date2 hold the same year, month, and day of month.
     */
    public static boolean isSameDate(@Nullable Date date1, @Nullable Date date2) {
        return (date1 == null) == (date2 == null)
                && (date1 == null
                || getDateOnly(date1).equals(getDateOnly(date2)));
    }

    /**
     * determines whether {@param date1} is before {@param date2}
     *
     * @param date1 Date object contains timestamp or date.
     * @param date2 Date object contains timestamp or date.
     * @return true, in case date1 is before date2 by comparing year, month, day of month.
     */
    public static boolean isFutureDate(@Nonnull Date date1, @Nonnull Date date2) {
        return getDateOnly(date1).before(getDateOnly(date2));
    }

}
