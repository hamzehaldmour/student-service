package com.rakbank.studentservice.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class CollectionUtils {

    @Nonnull
    public static <T> Collection<T> useEmptySetIfNull(@Nullable Collection<T> items) {
        return (Collection)(items == null ? Collections.emptySet() : items);
    }

    public static <T> Collection<T> useEmptyListIfNull(@Nullable Collection<T> items) {
        return (Collection)(items == null ? Collections.emptyList() : items);
    }

    @Nonnull
    public static <T> Set<T> subtract(@Nullable Collection<T> A, @Nullable Collection<T> B) {
        return subtract(A, B, HashSet::new);
    }

    @Nonnull
    public static <T, S extends Set<T>> S subtract(@Nullable Collection<T> A, @Nullable Collection<T> B, @Nonnull Supplier<S> setSupplier) {
        S result = setSupplier.get();
        if (A == null || A.isEmpty()) return result;

        result.addAll(A);
        if (B == null || B.isEmpty()) return result;

        result.removeAll(B);
        return result;
    }

    /**
     * outputs a triplet which contains the sets A - B, A ^ B, and B - A, in that order
     */
    @Nonnull
    public static <T> Triple<Set<T>, Set<T>, Set<T>> partitionSets(@Nullable Collection<T> A, @Nullable Collection<T> B) {
        return partitionSets(A, B, LinkedHashSet::new);
    }

    /**
     * outputs a triplet which contains the sets A - B, A ^ B, and B - A, in that order
     */
    @Nonnull
    public static <T, S extends Set<T>> Triple<S, S, S> partitionSets(@Nullable Collection<T> A, @Nullable Collection<T> B, @Nonnull Supplier<S> setSupplier) {
        final S aMinusB = setSupplier.get(),
                aAndB = setSupplier.get(),
                bMinusA = setSupplier.get();
        final Triple<S, S, S> partition = Triple.of(aMinusB, aAndB, bMinusA);
        final boolean empty_a = (A == null || A.isEmpty());
        if (empty_a != (B == null || B.isEmpty())) {
            if (empty_a) {
                bMinusA.addAll(B);
            } else {
                aMinusB.addAll(A);
            }
        } else if (!empty_a) {
            bMinusA.addAll(B);

            for (T item : A) {
                if (bMinusA.remove(item)) {
                    aAndB.add(item);
                } else {
                    aMinusB.add(item);
                }
            }
        }

        return partition;
    }

    /**
     * short circuits our partition algorithm for an equality comparison
     */
    public static <T> boolean equals(@Nullable Set<T> A, @Nullable Set<T> B) {
        final boolean empty_a = (A == null || A.isEmpty());
        if (empty_a != (B == null || B.isEmpty())) {
            return false;
        } else if (empty_a) {
            return true;
        } else if (A.size() != B.size()) {
            return false;
        }

        final Set<T> aMinusB = new HashSet<>(A);
        for (T item : B) {
            if (!aMinusB.remove(item)) {
                return false;
            }
        }

        return aMinusB.isEmpty();
    }

    public static int hashcode(@Nullable Collection<?> collection) {
        if (collection == null) {
            return 0;
        }

        int hashcode = 1;
        for (Object obj : collection) {
            hashcode = 31 * hashcode + HashcodeUtils.hashcode(obj);
        }
        return hashcode;
    }

    @Nonnull
    public static <T> List<Pair<T, T>> newPairwiseArrayList(T... keyValuePairs) {
        return newPairwiseList(() -> new ArrayList<>(keyValuePairs.length / 2), keyValuePairs);
    }

    @Nonnull
    public static <T, L extends List<Pair<T, T>>> L newPairwiseList(Supplier<L> listSupplier, T... keyValuePairs) {
        if ((keyValuePairs.length) % 2 == 1) {
            throw new IllegalArgumentException("Key/Value Array length must be even (was " + keyValuePairs.length + ")");
        }

        L list = listSupplier.get();
        for (int i=0; i<keyValuePairs.length; i+=2) {
            list.add(Pair.of(keyValuePairs[i], keyValuePairs[i+1]));
        }

        return list;
    }

    @Nonnull
    public static <T> TreeSet<T> newTreeSet(@Nonnull final Collection<T> collection,
                                            @Nonnull final Comparator<T> comparator) {
        final TreeSet<T> out = new TreeSet<>(comparator);
        out.addAll(collection);
        return out;
    }

    @Nonnull
    public static <T extends Comparable<T>> Collector<T, ?, TreeSet<T>> toTreeSet() {
        return Collectors.toCollection(TreeSet::new);
    }

    @Nonnull
    public static <T> Collector<T, ?, TreeSet<T>> toTreeSet(@Nonnull Comparator<? super T> comparator) {
        return Collector.of(() -> new TreeSet<>(comparator), Set::add, (left, right) -> {
            if (left.size() < right.size()) {
                right.addAll(left); return right;
            } else {
                left.addAll(right); return left;
            }
        });
    }

    @Nonnull
    public static <T> Stream<T> stream(@Nonnull Iterable<T> iterable, long estimatedSize) {
        return stream(iterable.iterator(), estimatedSize);
    }

    @Nonnull
    public static <T> Stream<T> stream(@Nonnull Iterator<T> iterator, long estimatedSize) {
        return StreamSupport.stream(Spliterators.spliterator(iterator, estimatedSize, Spliterator.IMMUTABLE), false);
    }

}
