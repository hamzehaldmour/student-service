package com.rakbank.studentservice.utils;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class MapUtils {

    @Nonnull
    public static <K, V> Map<K, V> useEmptyMapIfNull(@Nullable Map<K, V> map) {
        return useEmptyMapIfNull(map, Collections::emptyMap);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M useEmptyMapIfNull(@Nullable Map<K, V> map, @Nonnull Supplier<M> mapSupplier) {
        return (map == null ? mapSupplier.get() : (M) map);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key, @Nullable V value) {
        return createMap(key, value, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2) {
        return createMap(
                key1, value1,
                key2, value2, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3,
                                                     @Nullable K key4, @Nullable V value4) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3,
                                                     @Nullable K key4, @Nullable V value4,
                                                     @Nullable K key5, @Nullable V value5) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3,
                                                     @Nullable K key4, @Nullable V value4,
                                                     @Nullable K key5, @Nullable V value5,
                                                     @Nullable K key6, @Nullable V value6) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5,
                key6, value6, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3,
                                                     @Nullable K key4, @Nullable V value4,
                                                     @Nullable K key5, @Nullable V value5,
                                                     @Nullable K key6, @Nullable V value6,
                                                     @Nullable K key7, @Nullable V value7) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5,
                key6, value6,
                key7, value7, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nullable K key1, @Nullable V value1,
                                                     @Nullable K key2, @Nullable V value2,
                                                     @Nullable K key3, @Nullable V value3,
                                                     @Nullable K key4, @Nullable V value4,
                                                     @Nullable K key5, @Nullable V value5,
                                                     @Nullable K key6, @Nullable V value6,
                                                     @Nullable K key7, @Nullable V value7,
                                                     @Nullable K key8, @Nullable V value8) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5,
                key6, value6,
                key7, value7,
                key8, value8,
                HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> createHashMap(@Nonnull Collection<? extends Map.Entry<K, V>> entries) {
        return createMap(entries, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashMap<K, V> newHashMap(@Nonnull K[] keys, @Nonnull V[] values) {
        return newMap(keys, values, HashMap::new);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key, @Nullable V value) {
        return createMap(key, value, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2) {
        return createMap(
                key1, value1,
                key2, value2, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2,
                                                         @Nullable K key3, @Nullable V value3) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2,
                                                         @Nullable K key3, @Nullable V value3,
                                                         @Nullable K key4, @Nullable V value4) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2,
                                                         @Nullable K key3, @Nullable V value3,
                                                         @Nullable K key4, @Nullable V value4,
                                                         @Nullable K key5, @Nullable V value5) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2,
                                                         @Nullable K key3, @Nullable V value3,
                                                         @Nullable K key4, @Nullable V value4,
                                                         @Nullable K key5, @Nullable V value5,
                                                         @Nullable K key6, @Nullable V value6) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5,
                key6, value6, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nullable K key1, @Nullable V value1,
                                                         @Nullable K key2, @Nullable V value2,
                                                         @Nullable K key3, @Nullable V value3,
                                                         @Nullable K key4, @Nullable V value4,
                                                         @Nullable K key5, @Nullable V value5,
                                                         @Nullable K key6, @Nullable V value6,
                                                         @Nullable K key7, @Nullable V value7) {
        return createMap(
                key1, value1,
                key2, value2,
                key3, value3,
                key4, value4,
                key5, value5,
                key6, value6,
                key7, value7, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> createHashBiMap(@Nonnull Collection<? extends Map.Entry<K, V>> entries) {
        return createMap(entries, HashBiMap::create);
    }

    @Nonnull
    public static <K, V> HashBiMap<K, V> newHashBiMap(@Nonnull K[] keys, @Nonnull V[] values) {
        return newMap(keys, values, HashBiMap::create);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1),
                ArrayUtils.toArray(value1),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2),
                ArrayUtils.toArray(value1, value2),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3),
                ArrayUtils.toArray(value1, value2, value3),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nullable K key4, @Nullable V value4,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3, key4),
                ArrayUtils.toArray(value1, value2, value3, value4),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nullable K key4, @Nullable V value4,
                                                          @Nullable K key5, @Nullable V value5,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3, key4, key5),
                ArrayUtils.toArray(value1, value2, value3, value4, value5),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nullable K key4, @Nullable V value4,
                                                          @Nullable K key5, @Nullable V value5,
                                                          @Nullable K key6, @Nullable V value6,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3, key4, key5, key6),
                ArrayUtils.toArray(value1, value2, value3, value4, value5, value6),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nullable K key4, @Nullable V value4,
                                                          @Nullable K key5, @Nullable V value5,
                                                          @Nullable K key6, @Nullable V value6,
                                                          @Nullable K key7, @Nullable V value7,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3, key4, key5, key6, key7),
                ArrayUtils.toArray(value1, value2, value3, value4, value5, value6, value7),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nullable K key1, @Nullable V value1,
                                                          @Nullable K key2, @Nullable V value2,
                                                          @Nullable K key3, @Nullable V value3,
                                                          @Nullable K key4, @Nullable V value4,
                                                          @Nullable K key5, @Nullable V value5,
                                                          @Nullable K key6, @Nullable V value6,
                                                          @Nullable K key7, @Nullable V value7,
                                                          @Nullable K key8, @Nullable V value8,
                                                          @Nonnull Supplier<M> mapSupplier) {
        return newMap(
                ArrayUtils.toArray(key1, key2, key3, key4, key5, key6, key7, key8),
                ArrayUtils.toArray(value1, value2, value3, value4, value5, value6, value7, value8),
                mapSupplier);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M createMap(@Nonnull Collection<? extends Map.Entry<K, V>> entries,
                                                          @Nonnull Supplier<M> mapSupplier) {
        M map = mapSupplier.get();
        entries.forEach(e -> map.put(e.getKey(), e.getValue()));
        return map;
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M newMap(@Nonnull K[] keys, @Nonnull V[] values, @Nonnull Supplier<M> mapSupplier) {
        if (keys.length != values.length) {
            throw new IllegalArgumentException("Key Array length does not match Values Array length (" + keys.length + " =/= " + values.length + ")");
        }

        M map = mapSupplier.get();
        for (int i=0; i<keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    @Nonnull
    public static <T, M extends Map<T, T>> M newPairwiseMap(@Nonnull Supplier<M> mapSupplier, T... keyValuePairs) {
        if ((keyValuePairs.length) % 2 == 1) {
            throw new IllegalArgumentException("Key/Value Array length must be even (" + keyValuePairs.length + ")");
        }

        M map = mapSupplier.get();
        for (int i=0; i<keyValuePairs.length; i+=2) {
            map.put(keyValuePairs[i], keyValuePairs[i+1]);
        }
        return map;
    }

    @Nonnull
    public static <T extends Map.Entry<K, U>, K, U> Collector<T, ?, Map<K,U>> toHashMap() {
        return toHashMap(T::getKey, T::getValue);
    }

    @Nonnull
    public static <T, K, U> Collector<T, ?, Map<K,U>> toHashMap(@Nonnull final Function<? super T, ? extends K> keyMapper,
                                                                @Nonnull final Function<? super T, ? extends U> valueMapper) {
        return Collector.of(HashMap::new,
                (m, i) -> m.put(keyMapper.apply(i), valueMapper.apply(i)),
                (a, b) -> { a.putAll(b); return a; });
    }

    /**
     * short circuits our partition algorithm for an equality comparison
     */
    public static <K, V> boolean equals(@Nullable Map<K, V> A, @Nullable Map<K, V> B) {
        final boolean empty_a = (A == null || A.isEmpty());
        if (empty_a != (B == null || B.isEmpty())) {
            return false;
        } else if (empty_a) {
            return true;
        } else if (A.size() != B.size()) {
            return false;
        }

        final Set<K> aMinusB = new HashSet<>(A.keySet());
        for (Map.Entry<K, V> entry : B.entrySet()) {
            if (!aMinusB.remove(entry.getKey())) {
                return false;
            }

            V v_a = entry.getValue();
            V v_b = B.get(entry.getKey());
            if ((v_a == null) != (v_b == null)) {
                return false;
            } else if (v_a == null) {
                continue;
            }

            if (v_a instanceof Set) {
                if (!(v_b instanceof Set)
                        || !CollectionUtils.equals((Set) v_a, (Set) v_b)) {
                    return false;
                }
            } else if (v_a instanceof Multimap) {
                if (!(v_b instanceof Multimap)
                        || !MultimapUtils.equals((Multimap) v_a, (Multimap) v_b)) {
                    return false;
                }
            } else if (v_a instanceof Map) {
                if (!(v_b instanceof Map)
                        || !equals((Map) v_a, (Map) v_b)) {
                    return false;
                }
            } else if (v_a instanceof JSONObject) {
                if (!(v_b instanceof JSONObject)
                        || !JSONUtils.equals((JSONObject) v_a, (JSONObject) v_b)) {
                    return false;
                }
            } else if (v_a instanceof JSONArray) {
                if (!(v_b instanceof JSONArray)
                        || !JSONUtils.equals((JSONArray) v_a, (JSONArray) v_b)) {
                    return false;
                }
            } else if (!EqualityUtils.equals(v_a, v_b)) {
                return false;
            }
        }

        return aMinusB.isEmpty();
    }

    public static int hashcode(@Nullable Map<?, ?> o) {
        if (o == null) {
            return 0;
        }

        int hashcode = 1;
        for (Map.Entry<?, ?> entry : o.entrySet()) {
            hashcode = 31 * hashcode + HashcodeUtils.hashcode(entry.getKey());
            hashcode = 31 * hashcode + HashcodeUtils.hashcode(entry.getValue());
        }
        return hashcode;
    }
}
