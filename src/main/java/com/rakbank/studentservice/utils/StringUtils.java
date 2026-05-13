package com.rakbank.studentservice.utils;

import com.google.common.base.CharMatcher;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class StringUtils {

    public static boolean hasContent(@Nullable String input) {
        return !isNullOrEmptyOrWhitespace(input);
    }

    public static boolean isNullOrEmptyOrWhitespace(@Nullable String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * check if specified source string is quoted by any characters
     * defined in the "quotes" string. e.g.,
     * <p/>
     * -	isQuoted("'hello'", "'",  true)		return true;
     * -	isQuoted("<hello>", "<>", false)	return true;
     * -	isQuoted("<hello>", "<>", true)		return false;
     * -	isQuoted("ahelloa", "a",  true)		return true;
     *
     * @param src           source string to be checked.
     * @param quotes        string contains all characters as quote delimiters.
     * @param sameQuoteChar should force the checking of whether the first and last
     *                      are the same?
     * @return true if given src is quoted; false otherwise.
     */
    public static boolean isQuoted(@Nullable String src, @Nonnull String quotes, boolean sameQuoteChar) {
        if (quotes.isEmpty()) {
            throw new IllegalArgumentException("invalid quotes: " + quotes);
        }

        if (src == null
                || (src = src.trim()).isEmpty()) {
            return false;
        }

        assert src != null;
        char first = src.charAt(0);
        char last = src.charAt(src.length() - 1);

        if ((sameQuoteChar) && (first != last)) {
            return false;
        }

        return (quotes.indexOf(first) != -1) &&
                (quotes.indexOf(last) != -1);
    }

    public static boolean isAsciiOnly(String input) {
        for (char c : input.toCharArray()) {
            if (c > 0x7F) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public static String lPad(@Nullable String str, char padChar, int length) {
        if (str == null) {
            return null;
        } else {
            return str.length() >= length ? str : uniStr(padChar, length - str.length()) + str;
        }
    }

    @Nullable
    public static String rPad(@Nullable String str, char padChar, int length) {
        if (str == null) {
            return null;
        } else {
            return str.length() >= length ? str : str + uniStr(padChar, length - str.length());
        }
    }

    public static String uniStr(char ch, int length) {
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            result.append(ch);
        }

        return result.toString();
    }

    public static String indent(String str, char ch, int indent, boolean indentFirst) {
        return indent(str, uniStr(ch, indent), "\n", indentFirst);
    }

    public static String indent(String str, String indent, String splitRegex, boolean indentFirst) {
        return Arrays.stream(str.split(splitRegex))
                .map(s -> indent + s)
                .collect(Collectors.joining(splitRegex, indentFirst ? indent : "", ""));
    }

    @Nullable
    public static String toLowerCase(@Nullable String str) {
        return str == null
                ? null
                : str.toLowerCase();
    }

    public static boolean equalsIgnoreCase(@Nullable String a, @Nullable String b) {
        return (a == null) == (b == null)
                && (a == null
                || a.equalsIgnoreCase(b));
    }

    @Nonnull
    public static String toUpperCaseStart(@Nonnull String input) {
        if (!isNullOrEmptyOrWhitespace(input)) {
            char first = Character.toUpperCase(input.charAt(0));
            return input.length() > 1
                    ? first + input.substring(1)
                    : String.valueOf(first);
        }

        return input;
    }

    /**
     * a loop that splits all given strings by the given separator, and dumps all the results
     * into a single list.
     *
     * @param strings   - list of string to split
     * @param separator - character defining the break points
     * @return single list holding all split strings in the order they were encountered
     */
    @Nonnull
    public static List<String> split(@Nullable Collection<String> strings, char separator) {
        return strings == null
                ? Collections.emptyList()
                : strings.stream()
                .filter(s -> s != null && !s.isEmpty())
                .map(s -> s.split(Pattern.quote(String.valueOf(separator))))
                .filter(arr -> arr.length > 0)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    @Nonnull
    public static List<String> splitByCommonNamingConventions(@Nullable String s) {
        if (s == null
                || (s = s.trim()).isEmpty()) {
            return Collections.emptyList();
        }

        List<String> tokens = new ArrayList<>(s.length());
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            boolean endToken = false;
            boolean skipChar = false;
            if (Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
                endToken = true;
            } else if (Character.isWhitespace(c)
                    || c == '-'
                    || c == '_') {
                endToken = true;
                skipChar = true;
            }

            if (endToken && sb.length() > 0) {
                tokens.add(sb.toString());
                sb.setLength(0);
            }
            if (!skipChar) {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            tokens.add(sb.toString());
            sb.setLength(0);
        }

        return tokens;
    }

    private static final String[] TITLE_PREFIXES = {
            "The ",
            "A ",
            "An ",
            "Of ",
    };

    private static final String[] TITLE_PREFIXES_LC = {
            "the ",
            "a ",
            "an ",
            "of ",
    };

    @Nonnull
    public static String normalizeTitle(@Nonnull String origName, boolean caseSensitive) {
        String name = caseSensitive ? origName : origName.toLowerCase();
        String[] prefixes = caseSensitive ? TITLE_PREFIXES : TITLE_PREFIXES_LC;
        for (String prefix : prefixes) {
            if (name.startsWith(prefix)) {
                int pfxlen = prefix.length();
                return origName.substring(pfxlen) + ", " + origName.substring(0, pfxlen - 1);
            }
        }
        return origName;
    }

    @Nonnull
    public static String toTitleCase(@Nonnull String src) {
        return toTitleCase(src, " \t\n\r\f-");
    }

    public static String toPascalTitleCase(String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(name.charAt(0))).
                append(name.substring(1).toLowerCase());
        return sb.toString();
    }

    @Nonnull
    public static String toTitleCase(@Nonnull String src, @Nonnull String delim) {
        StringTokenizer st = new StringTokenizer(src, delim, true);
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            switch (token.length()) {
                case 1:
                    char ch = token.charAt(0);
                    if (Character.isWhitespace(ch)) {
                        sb.append(token);
                    } else {
                        sb.append(Character.toUpperCase(ch));
                    }
                    break;

                default:
                    sb.append(Character.toUpperCase(token.charAt(0))).
                            append(token.substring(1));
            }
        }
        return sb.toString();
    }

    @Nonnull
    public static Stream<Character> toCharStream(@Nonnull String s) {
        char[] arr = s.toCharArray();
        List<Character> l = new ArrayList<>(arr.length);
        for (char c : arr) l.add(c);
        return l.stream();
    }

    /**
     * if given src string starts with given prefix, then chop the
     * prefix; otherwise, original string will be returned.
     * <p/>
     * if either src or prefix is empty, original src will be returned.
     *
     * @param src    source string.
     * @param prefix prefix string.
     * @return prefix-chopped string.
     */
    @Nonnull
    public static String chopPrefix(@Nonnull String src, @Nonnull String prefix) {
        if (!(hasContent(src) && hasContent(prefix))) {
            return src;
        }
        return (src.startsWith(prefix)) ?
                src.substring(prefix.length()) :
                src;
    }

    /**
     * if given src string ends with given suffix, then chop the
     * suffix; otherwise, original string will be returned.
     * <p/>
     * if either src or suffix is empty, original src will be returned.
     *
     * @param src    source string.
     * @param suffix suffix string.
     * @return suffix-chopped string.
     */
    @Nonnull
    public static String chopSuffix(String src, String suffix) {
        if (!(hasContent(src) && hasContent(suffix))) {
            return src;
        }
        return (src.endsWith(suffix)) ?
                src.substring(0, src.length() - suffix.length()) :
                src;
    }

    ////

    /// Numbers --------------------------------------------------------------------------------------------------------
    //
    public static boolean isNumber(@Nullable String str) {
        return isNumber(str, true, false);
    }

    /**
     * integer the class of numbers, not integer the java datatype. does not preclude the
     * contained number from overrunning an int variable
     */
    public static boolean isInteger(@Nullable String str) {
        return isNumber(str, false, false);
    }

    public static boolean isFloat(@Nullable String str) {
        return isNumber(str, true, true);
    }

    public static boolean isNumber(@Nullable String str,
                                   boolean allowDecimal,
                                   final boolean requireDecimal) {
        if (str == null) return false;
        str = str.trim();
        if (str.isEmpty()) return false;
        if (requireDecimal) allowDecimal = true;

        boolean firstDigit = false;
        boolean negative = false;
        boolean foundDot = false;
        for (char c : str.toCharArray()) {
            switch (c) {
                case '.':
                    if (!allowDecimal || foundDot) return false;
                    foundDot = true;
                    break;

                case '-':
                    if (foundDot || firstDigit || negative) return false;
                    negative = true;
                    break;

                default:
                    if (CharMatcher.digit().matches(c)) {
                        if (!firstDigit) firstDigit = true;
                    } else {
                        return false;
                    }
            }
        }

        return firstDigit && (!requireDecimal || foundDot);
    }

    public static int parseInt(@Nullable String src) {
        return parseInt(src, -1);
    }

    public static int parseInt(@Nullable String src, int defaultValue) {
        if (src == null
                || (src = src.trim()).isEmpty()
                || !isInteger(src)) {
            return defaultValue;
        }

        return Integer.parseInt(src);
    }

    public static long parseLong(@Nullable String src) {
        return parseLong(src, -1);
    }

    public static long parseLong(@Nullable String src, long defaultValue) {
        if (src == null
                || (src = src.trim()).isEmpty()) {
            return defaultValue;
        }

        Long val = parseLong_nullSafe(src, 10);
        return val == null
                ? defaultValue
                : val;
    }

    @Nullable
    public static Long parseLong_nullSafe(@Nullable String src) {
        return parseLong_nullSafe(src, 10);
    }

    @Nullable
    public static Long parseLong_nullSafe(@Nullable String src, final int radix) {
        if (src == null
                || (src = src.trim()).isEmpty()) {
            return null;
        }

        // From Long.parseLong (without exceptions)
        boolean negative = false;
        int i = 0, len = src.length();
        long limit = -Long.MAX_VALUE;

        char firstChar = src.charAt(0);
        if (firstChar < '0') { // Possible leading "+" or "-"
            if (firstChar == '-') {
                negative = true;
                limit = Long.MIN_VALUE;
            } else if (firstChar != '+'
                    || len == 1) {
                return null;
            }
            i++;
        }

        long multmin = limit / radix;
        long result = 0;
        while (i < len) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            int digit = Character.digit(src.charAt(i++), radix);
            if (digit < 0 || result < multmin) {
                return null;
            }
            result *= radix;
            if (result < limit + digit) {
                return null;
            }
            result -= digit;
        }
        return negative ? result : -result;
    }
}
