package com.rakbank.studentservice.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class EnumUtils {

    private static final Map<Class<? extends Enum>, Set<? extends Enum>> VALUES_MAP = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T extends Enum<T>> Set<T> values(@Nonnull final Class<T> enumClass) {
        return (Set<T>) VALUES_MAP.computeIfAbsent(enumClass, c -> Collections.unmodifiableSet(EnumSet.allOf(c)));
    }

    @Nullable
    public static <T extends Enum<T>> T findByName(@Nullable final String name,
                                                   @Nonnull final Class<T> enumClass) {
        return findByName(name, enumClass, true, true, false);
    }

    @Nullable
    public static <T extends Enum<T>> T findByName(@Nullable String name,
                                                   @Nonnull final Class<T> enumClass,
                                                   final boolean caseSensitive) {
        return findByName(name, enumClass, caseSensitive, true, false);
    }

    @Nullable
    public static <T extends Enum<T>> T findByName(@Nullable String name,
                                                   @Nonnull final Class<T> enumClass,
                                                   final boolean caseSensitive,
                                                   final boolean trim,
                                                   final boolean normalizeSeparators) {
        if (name == null) {
            return null;
        }

        if (!caseSensitive) name = name.toLowerCase();
        if (trim) name = name.trim();
        if (normalizeSeparators) name = name.replaceAll("[-_ ]", "");

        return findMatching(name, enumClass, caseSensitive
                ? normalizeSeparators
                    ? ((Function<String, String>) (String n) -> n.replaceAll("_", "")).compose(Enum::name)
                    : Enum::name
                : normalizeSeparators
                    ? ((Function<String, String>) (String n) -> n.toLowerCase().replaceAll("_", "")).compose(Enum::name)
                    : ((Function<String, String>) String::toLowerCase).compose(Enum::name),
                String::equals);
    }

    @Nullable
    public static <T extends Enum<T>> T findByOrdinal(final int value,
                                                      @Nonnull final Class<T> enumClass) {
        return findByOrdinalImpl(value, enumClass, null);
    }

    @SuppressWarnings("all")
    @Nonnull
    public static <T extends Enum<T>> T findByOrdinal(final int value,
                                                      @Nonnull final Class<T> enumClass,
                                                      @Nonnull final T defaultValue) {
        return findByOrdinalImpl(value, enumClass, defaultValue);
    }

    @Nullable
    private static <T extends Enum<T>> T findByOrdinalImpl(final int value,
                                                           @Nonnull final Class<T> enumClass,
                                                           @Nullable final T defaultValue) {
        if (value < 0 || value > values(enumClass).size()) {
            return defaultValue;
        }

        return findMatchingImpl(value, enumClass,
                Enum::ordinal,
                Objects::equals,
                defaultValue);
    }

    @Nullable
    public static <T extends Enum<T>, V1, V2> T findMatching(@Nullable final V2 value,
                                                             @Nonnull final Class<T> enumClass,
                                                             @Nonnull final Function<T, V1> transformation,
                                                             @Nonnull final BiPredicate<V1, V2> matcher) {
        return findMatchingImpl(value, enumClass, transformation, matcher, null);
    }

    @SuppressWarnings("all")
    @Nonnull
    public static <T extends Enum<T>, V1, V2> T findMatching(@Nullable final V2 value,
                                                            @Nonnull final Class<T> enumClass,
                                                            @Nonnull final Function<T, V1> transformation,
                                                            @Nonnull final BiPredicate<V1, V2> matcher,
                                                            @Nonnull final T defaultValue) {
        return findMatchingImpl(value, enumClass, transformation, matcher, defaultValue);
    }

    @Nullable
    private static <T extends Enum<T>, V1, V2> T findMatchingImpl(@Nullable final V2 value,
                                                                  @Nonnull final Class<T> enumClass,
                                                                  @Nonnull final Function<T, V1> transformation,
                                                                  @Nonnull final BiPredicate<V1, V2> matcher,
                                                                  @Nullable final T defaultValue) {
        for (T item : values(enumClass)) {
            // this order makes things slightly more null-safe (item THEN value)
            if (matcher.test(transformation.apply(item), value)) {
                return item;
            }
        }

        return defaultValue;
    }

    @Nullable
    public static <T extends Enum<T>> T findMatching(@Nonnull final Class<T> enumClass,
                                                     final boolean conjunctive,
                                                     @Nonnull final Pair<Function<T, ?>, Predicate<?>>... matchers) {
        nextItem:
        for (T item : values(enumClass)) {
            for (Pair<Function<T, ?>, Predicate<?>> matcher : matchers) {
                if (test(item, matcher.getLeft(), matcher.getRight())) {
                    if (!conjunctive) return item;
                } else if (conjunctive) {
                    continue nextItem;
                }
            }

            if (conjunctive) {
                return item;
            }
        }

        return null;
    }

    private static <T extends Enum<T>, V> boolean test(@Nonnull final T item,
                                                       @Nonnull final Function<T, ?> transformation,
                                                       @Nonnull final Predicate<V> predicate) {
        return predicate.test((V) transformation.apply(item));
    }

}
