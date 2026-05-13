package com.rakbank.studentservice.utils;

import com.google.common.collect.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
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
public class JSONUtils {

    private static final Set<Extension> TO_JSON_EXTENSIONS = new HashSet<>();

    private JSONUtils() {}

    @Nonnull
    public static JSONObject clear(@Nonnull JSONObject obj) {
        new ArrayList<>(obj.keySet())
                .forEach(obj::remove);
        return obj;
    }

    @Nonnull
    public static JSONObject clone(@Nonnull JSONObject orig) {
        JSONObject clone = new JSONObject();
        for (String key : orig.keySet()) {
            Object obj = orig.get(key);
            if (obj instanceof JSONObject) {
                clone.put(key, clone((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                clone.put(key, clone((JSONArray) obj));
            } else {
                clone.put(key, obj);
            }
        }

        return clone;
    }

    @Nonnull
    public static JSONArray clone(@Nonnull JSONArray orig) {
        JSONArray clone = new JSONArray();
        for (int i=0; i<orig.length(); i++) {
            Object obj = orig.get(i);
            if (obj instanceof JSONObject) {
                clone.put(i, clone((JSONObject) obj));
            } else if (obj instanceof JSONArray) {
                clone.put(i, clone((JSONArray) obj));
            } else {
                clone.put(i, obj);
            }
        }

        return clone;
    }

    @Nonnull
    public static Object clone(@Nonnull Object orig) {
        if (orig instanceof JSONArray) {
            return clone((JSONArray) orig);
        } else if (orig instanceof JSONObject) {
            return clone((JSONObject) orig);
        } else if (orig instanceof Collection) {
            return clone(new JSONArray((Collection<?>) orig));
        } else if (orig instanceof Map) {
            return clone(new JSONObject((Map<?, ?>) orig));
        } else {
            // primitive, no clone needed
            return orig;
        }
    }

    @Nonnull
    public static JSONObject getAsJSONObject(@Nonnull JSONArray array) {
        JSONObject out = new JSONObject();
        for (int i=0; i<array.length(); i++) {
            out.put(String.valueOf(i), array.get(i));
        }
        return out;
    }

    @Nonnull
    public static <T> Stream<T> getStream(@Nonnull String param, @Nullable JSONObject source) {
        return stream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null));
    }

    @Nonnull
    public static Stream<Integer> getIntStream(@Nonnull String param, @Nullable JSONObject source) {
        return intStream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null));
    }

    @Nonnull
    public static Stream<Long> getLongStream(@Nonnull String param, @Nullable JSONObject source) {
        return longStream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null));
    }

    @Nonnull
    public static Stream<Float> getFloatStream(@Nonnull String param, @Nullable JSONObject source) {
        return floatStream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null));
    }

    @Nonnull
    public static Stream<Double> getDoubleStream(@Nonnull String param, @Nullable JSONObject source) {
        return doubleStream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null));
    }

    @Nonnull
    public static <T> Stream<T> getStream(@Nonnull String param, @Nullable JSONObject source, @Nonnull Function<JSONObject, T> transformer) {
        return stream(Optional.ofNullable(source)
                .map(json -> json.opt(param))
                .orElse(null), transformer);
    }

    @Nonnull
    public static <T> Stream<T> stream(@Nullable Object jsonValue) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray arr = (JSONArray) jsonValue;
            List<T> list = new ArrayList<>(arr.length());
            for (int i=0; i<arr.length(); i++) {
                list.add((T) arr.get(i));
            }
            return list.stream();
        } else {
            return Stream.of((T) jsonValue);
        }
    }

    @Nonnull
    public static Stream<Integer> intStream(@Nullable Object jsonValue) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray arr = (JSONArray) jsonValue;
            List<Integer> list = new ArrayList<>(arr.length());
            for (int i=0; i<arr.length(); i++) {
                list.add(arr.getInt(i));
            }
            return list.stream();
        } else if (jsonValue instanceof Number) {
            return Stream.of(((Number) jsonValue).intValue());
        } else if (jsonValue instanceof String) {
            return Stream.of(Integer.parseInt((String) jsonValue));
        } else {
            throw new ClassCastException(jsonValue.getClass().getSimpleName() + " cannot be treated as an int");
        }
    }

    @Nonnull
    public static Stream<Long> longStream(@Nullable Object jsonValue) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray arr = (JSONArray) jsonValue;
            List<Long> list = new ArrayList<>(arr.length());
            for (int i=0; i<arr.length(); i++) {
                list.add(arr.getLong(i));
            }
            return list.stream();
        } else if (jsonValue instanceof Number) {
            return Stream.of(((Number) jsonValue).longValue());
        } else if (jsonValue instanceof String) {
            return Stream.of(Long.parseLong((String) jsonValue));
        } else {
            throw new ClassCastException(jsonValue.getClass().getSimpleName() + " cannot be treated as a long");
        }
    }

    @Nonnull
    public static Stream<Float> floatStream(@Nullable Object jsonValue) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray arr = (JSONArray) jsonValue;
            List<Float> list = new ArrayList<>(arr.length());
            for (int i=0; i<arr.length(); i++) {
                list.add(Double.valueOf(arr.getDouble(i)).floatValue());
            }
            return list.stream();
        } else if (jsonValue instanceof Number) {
            return Stream.of(((Number) jsonValue).floatValue());
        } else if (jsonValue instanceof String) {
            return Stream.of(Float.parseFloat((String) jsonValue));
        } else {
            throw new ClassCastException(jsonValue.getClass().getSimpleName() + " cannot be treated as a float");
        }
    }

    @Nonnull
    public static Stream<Double> doubleStream(@Nullable Object jsonValue) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray arr = (JSONArray) jsonValue;
            List<Double> list = new ArrayList<>(arr.length());
            for (int i=0; i<arr.length(); i++) {
                list.add(arr.getDouble(i));
            }
            return list.stream();
        } else if (jsonValue instanceof Number) {
            return Stream.of(((Number) jsonValue).doubleValue());
        } else if (jsonValue instanceof String) {
            return Stream.of(Double.parseDouble((String) jsonValue));
        } else {
            throw new ClassCastException(jsonValue.getClass().getSimpleName() + " cannot be treated as a double");
        }
    }

    @Nonnull
    public static <T> Stream<T> stream(@Nullable Object jsonValue, @Nonnull Function<JSONObject, T> transformer) {
        if (jsonValue == null) {
            return Stream.empty();
        } else if (jsonValue instanceof JSONArray) {
            JSONArray a = (JSONArray) jsonValue;
            return StreamSupport.stream(Spliterators.spliterator(a.iterator(), a.length(), Spliterator.IMMUTABLE), false)
                    .map(o -> (JSONObject) o)
                    .map(transformer);
        } else if (jsonValue instanceof JSONObject) {
            return Stream.of((JSONObject) jsonValue).map(transformer);
        } else {
            throw new ClassCastException("jsonValue " + jsonValue + " was not a JSONObject or JSONArray");
        }
    }

    @Nonnull
    public static <T> Stream<Pair<String, T>> stream(@Nullable JSONObject jsonObj) {
        return jsonObj == null
                ? Stream.empty()
                : jsonObj.keySet().stream().map(key -> Pair.of(key, (T) jsonObj.get(key)));
    }

    @Nonnull
    public static Stream<Pair<String, Object>> flatStream(@Nullable JSONObject jsonObj) {
        return jsonObj == null
                ? Stream.empty()
                : jsonObj.keySet().stream().flatMap(key -> getStream(key, jsonObj).map(value -> Pair.of(key, value)));
    }

    @Nonnull
    public static <T> Iterable<T> getIterable(@Nonnull String param, @Nullable JSONObject source) {
        return new Iterable<T>() {
            @Override
            @Nonnull
            public Iterator<T> iterator() {
                return JSONUtils.<T>getStream(param, source).iterator();
            }
        };
    }

    @Nonnull
    public static <T> Iterable<T> getIterable(@Nonnull String param, @Nullable JSONObject source, @Nonnull Function<JSONObject, T> transformer) {
        return new Iterable<T>() {
            @Override
            @Nonnull
            public Iterator<T> iterator() {
                return getStream(param, source, transformer).iterator();
            }
        };
    }

    public static void addJsonData(@Nonnull JSONObject json, @Nonnull JSONObject newJson) {
        for (String key : newJson.keySet()) {
            Object newValue = newJson.get(key);
            Object existingValue = json.opt(key);
            if (existingValue == null) {
                json.put(key, newValue);
            } else if (newValue != null) {
                if (existingValue instanceof JSONArray) {
                    Set<Object> existingValues = stream(existingValue).collect(Collectors.toSet());
                    if (newValue instanceof JSONArray) {
                        stream(newValue).forEach(existingValues::add);
                    } else {
                        existingValues.add(newValue);
                    }
                    json.put(key, existingValues);
                } else if (existingValue instanceof JSONObject) {
                    addJsonData((JSONObject) existingValue, (JSONObject) newValue);
                }
            }
        }
    }

    @Nonnull
    public static <V> Map<String, V> toMap(@Nonnull JSONObject json) {
        return toMap(json, HashMap::new, (s, o) -> s, (s, o) -> (V) o);
    }

    @Nonnull
    public static <K, V> Map<K, V> toMap(@Nonnull JSONObject json,
                                         @Nonnull BiFunction<String, Object, K> keyFactory) {
        return toMap(json, HashMap::new, keyFactory, (s, o) -> (V) o);
    }

    @Nonnull
    public static <K, V, M extends Map<K, V>> M toMap(@Nonnull JSONObject json,
                                                      @Nonnull Supplier<M> mapSupplier,
                                                      @Nonnull BiFunction<String, Object, K> keyFactory,
                                                      @Nonnull BiFunction<String, Object, V> valueFactory) {
        M map = mapSupplier.get();
        stream(json).forEach(p -> map.put(
                keyFactory.apply(p.getLeft(), p.getRight()),
                valueFactory.apply(p.getLeft(), p.getRight())));
        return map;
    }

    @Nonnull
    public static ListMultimap<String, Object> toListMultimap(@Nonnull JSONObject json,
                                                              @Nonnull Supplier<Map<String, Collection<Object>>> mapSupplier,
                                                              @Nonnull Supplier<? extends List<Object>> factory) {
        ListMultimap<String, Object> map = Multimaps.newListMultimap(mapSupplier.get(), factory::get);
        fillMultimap(json, map);
        return map;
    }

    @Nonnull
    public static SetMultimap<String, Object> toSetMultimap(@Nonnull JSONObject json,
                                                            @Nonnull Supplier<Map<String, Collection<Object>>> mapSupplier,
                                                            @Nonnull Supplier<? extends Set<Object>> factory) {
        SetMultimap<String, Object> map = Multimaps.newSetMultimap(mapSupplier.get(), factory::get);
        fillMultimap(json, map);
        return map;
    }

    public static void fillMultimap(@Nonnull JSONObject json, @Nonnull Multimap<String, Object> multimap) {
        flatStream(json).forEach(p -> multimap.put(p.getLeft(), p.getRight()));
    }

    public static int getInt(@Nonnull String param, @Nonnull JSONObject json, int defaultValue) {
        return Optional.of(json)
                .filter(o -> o.has(param))
                .map(o -> o.get(param))
                .map(o -> o instanceof Number
                        ? (Number) o
                        : o instanceof String ? StringUtils.parseInt((String) o, defaultValue) : defaultValue)
                .map(Number::intValue)
                .orElse(defaultValue);
    }

    public static long getLong(@Nonnull String param, @Nonnull JSONObject json, long defaultValue) {
        return Optional.of(json)
                .filter(o -> o.has(param))
                .map(o -> o.get(param))
                .map(o -> o instanceof Number
                        ? (Number) o
                        : o instanceof String ? StringUtils.parseLong((String) o, defaultValue) : defaultValue)
                .map(Number::longValue)
                .orElse(defaultValue);
    }

    public static float getFloat(@Nonnull String param, @Nonnull JSONObject json, float defaultValue) {
        return Optional.of(json)
                .filter(o -> o.has(param))
                .map(o -> o.get(param))
                .map(o -> {
                    if (o instanceof String) {
                        try {
                            return Float.parseFloat((String) o);
                        } catch (NumberFormatException ex) {}
                    }

                    return o instanceof Number
                            ? (Number) o
                            : defaultValue;
                }).map(Number::floatValue)
                .orElse(defaultValue);
    }

    public static double getDouble(@Nonnull String param, @Nonnull JSONObject json, double defaultValue) {
        return Optional.of(json)
                .filter(o -> o.has(param))
                .map(o -> o.get(param))
                .map(o -> {
                    if (o instanceof String) {
                        try {
                            return Double.parseDouble((String) o);
                        } catch (NumberFormatException ex) {}
                    }

                    return o instanceof Number
                            ? (Number) o
                            : defaultValue;
                }).map(Number::doubleValue)
                .orElse(defaultValue);
    }

    @Nonnull
    public static JSONObject getOrCreateJSONObject(@Nonnull String key, @Nonnull JSONObject json) {
        return getOrCreateJSONObject(key, json, JSONObject::new);
    }

    @Nonnull
    public static JSONObject getOrCreateJSONObject(@Nonnull String key, @Nonnull JSONObject json, @Nonnull Supplier<JSONObject> jsonObjectFactory) {
        if (!json.has(key)) {
            synchronized (json) {
                if (!json.has(key)) {
                    json.put(key, jsonObjectFactory.get());
                }
            }
        }

        return json.getJSONObject(key);
    }

    @Nonnull
    public static JSONArray getOrCreateJSONArray(@Nonnull String key, @Nonnull JSONObject json) {
        return getOrCreateJSONArray(key, json, JSONArray::new);
    }

    @Nonnull
    public static JSONArray getOrCreateJSONArray(@Nonnull String key, @Nonnull JSONObject json, @Nonnull Supplier<JSONArray> jsonArrayFactory) {
        if (!json.has(key)) {
            synchronized (json) {
                if (!json.has(key)) {
                    json.put(key, jsonArrayFactory.get());
                }
            }
        }

        return json.getJSONArray(key);
    }

    public static <T> boolean addToArrayAsSet(@Nonnull T value, @Nonnull JSONArray array) {
        if (stream(array).noneMatch(item -> item.equals(value) || normalizeToString(item).equals(normalizeToString(value)))) {
            array.put(value);
            return true;
        }

        return false;
    }

    @Nonnull
    public static JSONArray addAllToArrayAsSet(@Nonnull JSONArray array, @Nonnull JSONArray newValues) {
        stream(newValues).forEach(val -> addToArrayAsSet(val, array));
        return array;
    }

    public static boolean addAll(@Nonnull JSONObject currentJson,
                                 @Nonnull JSONObject newJson) {
        return addAll(currentJson, newJson, SingletonValueKeyMatchHandler.OVERWRITE, PluralValueKeyMatchHandler.ACCUMULATE_AS_SET);
    }

    public static boolean addAll(@Nonnull JSONObject currentJson,
                                 @Nonnull JSONObject newJson,
                                 @Nonnull SingletonValueKeyMatchHandler singletonMatchHandler,
                                 @Nonnull IPluralValueKeyMatchHandler pluralMatchHandler) {
        return addAll(currentJson, newJson, null, Collections.emptyList(),
                path -> singletonMatchHandler,
                path -> pluralMatchHandler,
                null);
    }

    public static boolean addAll(@Nonnull JSONObject currentJson,
                                 @Nonnull JSONObject newJson,
                                 @Nullable JSONObject unusedJson_out,
                                 @Nonnull List<String> currentPath,
                                 @Nonnull Function<List<String>, SingletonValueKeyMatchHandler> singletonMatchHandlerFactory,
                                 @Nonnull Function<List<String>, IPluralValueKeyMatchHandler> pluralMatchHandlerFactory,
                                 @Nullable Predicate<List<String>> keyValidator) {
        boolean changed = false;
        Set<String> paramsHandled = new HashSet<>();
        for (String param : newJson.keySet()) {
            final List<String> newPath = Collections.unmodifiableList(Stream.concat(currentPath.stream(), Stream.of(param)).collect(Collectors.toList()));
            if (keyValidator != null && !keyValidator.test(newPath)) {
                continue;
            }

            paramsHandled.add(param);
            if (!currentJson.has(param)) {
                currentJson.put(param, newJson.get(param));
                changed = true;
                continue;
            }

            if (newJson.get(param) instanceof JSONObject) {
                JSONObject remainingNextLevelDetails = unusedJson_out == null ? null : new JSONObject();
                changed |= addAll(currentJson.getJSONObject(param), newJson.getJSONObject(param), remainingNextLevelDetails, newPath,
                        singletonMatchHandlerFactory, pluralMatchHandlerFactory, keyValidator);
                if (unusedJson_out != null && !unusedJson_out.keySet().isEmpty()) {
                    unusedJson_out.put(param, remainingNextLevelDetails);
                }
                continue;
            }

            // we've got a match
            Object val_a = currentJson.get(param);
            Object val_b = newJson.get(param);
            if (val_a instanceof JSONArray || val_b instanceof JSONArray) {
                IPluralValueKeyMatchHandler handler = Optional.ofNullable(pluralMatchHandlerFactory.apply(newPath))
                        .orElse(PluralValueKeyMatchHandler.ACCUMULATE_AS_SET);
                if (handler instanceof MatchOnObjectKey) {
                    changed |= ((MatchOnObjectKey) handler).handle(newPath, currentJson, val_a, val_b, unusedJson_out, singletonMatchHandlerFactory, pluralMatchHandlerFactory, keyValidator);
                } else {
                    changed |= handler.handle(newPath, currentJson, val_a, val_b);
                }
            } else {
                if (!compatibleJsonValues(val_a, val_b)) {
                    SingletonValueKeyMatchHandler handler = Optional.ofNullable(singletonMatchHandlerFactory.apply(newPath))
                            .orElse(SingletonValueKeyMatchHandler.OVERWRITE);
                    changed |= handler.handle(newPath, currentJson, val_a, val_b);
                }
            }
        }

        if (unusedJson_out != null) {
            for (String param : newJson.keySet()) {
                if (paramsHandled.add(param)) {
                    unusedJson_out.put(param, newJson.get(param));
                }
            }
        }

        return changed;
    }

    public static <T> boolean removeFromArray(@Nonnull T value, @Nullable JSONArray array) {
        if (array == null) {
            return false;
        }

        for (int i=0; i<array.length(); i++) {
            Object item = array.get(i);
            if (item.equals(value) || normalizeToString(item).equals(value)) {
                array.remove(i);
                return true;
            }
        }

        return false;
    }

    public static boolean equals(@Nonnull JSONObject a, @Nonnull JSONObject b) {
        Triple<JSONObject, JSONObject, JSONObject> partition = partition(a, b);
        return partition.getLeft().keySet().isEmpty() && partition.getRight().keySet().isEmpty();
    }

    public static Triple<JSONObject, JSONObject, JSONObject> partition(@Nonnull JSONObject a, @Nonnull JSONObject b) {
        Set<String> keys_a = a.keySet();
        Set<String> keys_b = new LinkedHashSet<>(b.keySet());
        Iterator<String> keyIterator = keys_a.iterator();
        JSONObject left = new JSONObject(),
                intersection = new JSONObject(),
                right = new JSONObject();

        String key;
        while(keyIterator.hasNext()) {
            key = keyIterator.next();
            if (keys_b.remove(key)) {
                Object value_a = a.get(key);
                Object value_b = b.get(key);

                if (value_a instanceof JSONObject) {
                    Triple<JSONObject, JSONObject, JSONObject> partition = partition((JSONObject) value_a, (JSONObject) value_b);
                    if (!partition.getLeft().keySet().isEmpty()) left.put(key, partition.getLeft());
                    if (!partition.getMiddle().keySet().isEmpty()) intersection.put(key, partition.getMiddle());
                    if (!partition.getRight().keySet().isEmpty()) right.put(key, partition.getRight());
                } else if (value_a instanceof JSONArray) {
                    Triple<JSONArray, JSONArray, JSONArray> partition = partition((JSONArray) value_a, (JSONArray) value_b);
                    if (partition.getLeft().length() > 0) left.put(key, partition.getLeft());
                    if (partition.getMiddle().length() > 0) intersection.put(key, partition.getMiddle());
                    if (partition.getRight().length() > 0) right.put(key, partition.getRight());
                } else {
                    // primitive comparison
                    if (compatibleJsonValues(value_a, value_b)) {
                        intersection.put(key, value_a);
                    } else {
                        left.put(key, value_a);
                        right.put(key, value_b);
                    }
                }
            } else {
                left.put(key, clone(a.get(key)));
            }
        }

        keyIterator = keys_b.iterator();
        while(keyIterator.hasNext()) {
            key = keyIterator.next();
            right.put(key, clone(b.get(key)));
        }

        return Triple.of(left, intersection, right);
    }

    public static boolean equals(@Nonnull JSONArray a, @Nonnull JSONArray b) {
        Triple<JSONArray, JSONArray, JSONArray> partition = partition(a, b);
        return partition.getLeft().length() == 0 && partition.getRight().length() == 0;
    }

    public static Triple<JSONArray, JSONArray, JSONArray> partition(@Nonnull JSONArray a, @Nonnull JSONArray b) {
        final JSONArray left = new JSONArray(),
                intersection = new JSONArray(),
                right = new JSONArray();

        final List items_a = stream(a).collect(Collectors.toList());
        final List items_b = stream(b).collect(Collectors.toList());
        final Pair<List<String>, Triple<JSONObject, JSONObject, JSONObject>> keyBasedPartition = findBestKeyBasedPartitionForObjects(items_a, items_b);

        // clear out simple matches first
        final Iterator iter_a = items_a.iterator();
        while (iter_a.hasNext()) {
            Object current_a = iter_a.next();
            if (current_a instanceof JSONObject
                    && keyBasedPartition != null) {

                Triple<JSONObject, JSONObject, JSONObject> match = findMatching((JSONObject) current_a, items_b, keyBasedPartition.getLeft(), keyBasedPartition.getRight());
                if (match != null) {
                    if (match.getLeft() != null) left.put(match.getLeft());
                    if (match.getMiddle() != null) intersection.put(match.getMiddle());
                    if (match.getRight() != null) right.put(match.getRight());
                } else {
                    left.put(current_a);
                }
            } else if (current_a instanceof JSONArray) {
                Triple<JSONArray, JSONArray, JSONArray> match = findMatching((JSONArray) current_a, items_b);
                if (match != null) {
                    left.put(match.getLeft());
                    intersection.put(match.getMiddle());
                    right.put(match.getRight());
                } else {
                    left.put(current_a);
                }
            } else {
                Object match = findMatchingObj(current_a, items_b);
                if (match != null) {
                    intersection.put(match);
                } else {
                    left.put(current_a);
                }
            }

            iter_a.remove();
        }

        for (Object o : items_b) {
            right.put(o);
        }

        return Triple.of(left, intersection, right);
    }

    @Nullable
    private static Triple<JSONObject, JSONObject, JSONObject> findMatching(@Nonnull JSONObject obj_a,
                                                                           @Nonnull List items_b,
                                                                           @Nonnull List<String> compositeKey,
                                                                           @Nonnull Triple<JSONObject, JSONObject, JSONObject> keyBasedPartition) {
        String keyVal_a = extractKey(compositeKey, obj_a);
        Iterator iter_b = items_b.iterator();
        while (iter_b.hasNext()) {
            Object current_b = iter_b.next();

            if (current_b instanceof JSONObject
                    && keyVal_a.equals(extractKey(compositeKey, (JSONObject) current_b))) {
                iter_b.remove();
                return Triple.of(
                        keyBasedPartition.getLeft().optJSONObject(keyVal_a),
                        keyBasedPartition.getMiddle().optJSONObject(keyVal_a),
                        keyBasedPartition.getRight().optJSONObject(keyVal_a));
            }
        }

        return null;
    }

    @Nullable
    private static Triple<JSONArray, JSONArray, JSONArray> findMatching(@Nonnull JSONArray arr_a, @Nonnull List items_b) {
        JSONArray bestMatch = null;
        double bestScore = -1;
        Triple<JSONArray, JSONArray, JSONArray> bestPartition = null;

        for (Object current_b : items_b) {
            if (current_b instanceof JSONArray) {
                JSONArray arr_b = (JSONArray) current_b;
                Triple<JSONArray, JSONArray, JSONArray> partition = partition(arr_a, arr_b);

                double score = gradeArrayPartition(partition);

                if (score > 0
                        && (bestPartition == null
                            || bestScore < score)) {
                    bestMatch = arr_b;
                    bestScore = score;
                    bestPartition = partition;
                }
            }
        }

        if (bestMatch == null) return null;

        Iterator iter_b = items_b.iterator();
        while (iter_b.hasNext()) {
            if (bestMatch == iter_b.next()) {
                iter_b.remove();
                break;
            }
        }

        return bestPartition;
    }

    private static double gradeArrayPartition(Triple<JSONArray, JSONArray, JSONArray> partition) {
        int total = partition.getLeft().length() + partition.getMiddle().length() + partition.getRight().length();
        return total == 0
                ? 0
                : ((double) partition.getMiddle().length() / (double) total);
    }

    @Nullable
    private static Object findMatchingObj(@Nonnull Object obj_a, @Nonnull List items_b) {
        Iterator iter_b = items_b.iterator();
        while (iter_b.hasNext()) {
            Object current_b = iter_b.next();
            if (compatibleJsonValues(obj_a, current_b)) {
                iter_b.remove();
                return current_b;
            }
        }

        return null;
    }

    @Nullable
    private static Pair<List<String>, Triple<JSONObject, JSONObject, JSONObject>> findBestKeyBasedPartitionForObjects(@Nonnull List items_a, @Nonnull List items_b) {
        if (items_a.isEmpty() || items_b.isEmpty()) {
            return null;
        }

        Set<String> keysSharedByAll = null;
        final List<JSONObject> objects_a = new ArrayList<>(items_a.size());
        final List<JSONObject> objects_b = new ArrayList<>(items_b.size());

        for (Object obj : items_a) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) obj;
                objects_a.add(jsonObj);

                if (keysSharedByAll == null) {
                    keysSharedByAll = new TreeSet<>(jsonObj.keySet());
                } else {
                    keysSharedByAll.retainAll(jsonObj.keySet());
                }
                if (keysSharedByAll.isEmpty()) break;
            }
        }

        if (keysSharedByAll == null || keysSharedByAll.isEmpty()) {
            return null;
        }

        for (Object obj : items_b) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) obj;
                objects_b.add(jsonObj);

                keysSharedByAll.retainAll(jsonObj.keySet());
                if (keysSharedByAll.isEmpty()) break;
            }
        }

        new ArrayList<>(keysSharedByAll)
                .stream()
                .filter(k -> Optional.of(k)
                        .map(String::toLowerCase)
                        .filter(_k -> _k.contains("date") || _k.contains("time") || _k.contains("timing") || _k.contains("duration"))
                        .isPresent())
                .forEach(keysSharedByAll::remove);

        if (keysSharedByAll.isEmpty()
                || objects_a.isEmpty()
                || objects_b.isEmpty()) {
            return null;
        }

        final Set<List<String>> possibleCompositeKeys = new HashSet<>();
        final Set<List<String>> compositeKeysExamined = new HashSet<>();
        final Set<List<String>> rejects = new HashSet<>();
        rejects.add(Collections.emptyList());

        for (int keySize = 1; keySize <= keysSharedByAll.size(); keySize++) {
            Set<List<String>> prefixes = new HashSet<>(rejects);
            rejects.clear();

            for (List<String> keyPrefix : prefixes) {
                for (String newKeyElement : (keySize == 1
                        ? new ArrayList<>(keysSharedByAll)
                        : keysSharedByAll)) {
                    if (keyPrefix.stream().anyMatch(partialKey -> newKeyElement.compareTo(partialKey) <= 0)) {
                        continue;
                    }

                    List<String> compositeKey = new ArrayList<>(keyPrefix);
                    compositeKey.add(newKeyElement);
                    if (compositeKeysExamined.add(compositeKey)) {
                        boolean canIndex = canIndexOverKey(compositeKey, objects_a) && canIndexOverKey(compositeKey, objects_b);
                        (canIndex
                                ? possibleCompositeKeys
                                : rejects).add(compositeKey);
                        if (canIndex && keySize == 1) {
                            keysSharedByAll.remove(newKeyElement);
                        }
                    }
                }
            }

            if (rejects.isEmpty()) break;
        }

        if (possibleCompositeKeys.isEmpty()) {
            return null;
        }

        // which key has the best overlap?
        List<String> bestKey = null;
        double bestScore = -1;
        Triple<JSONObject, JSONObject, JSONObject> bestPartition = null;
        for (List<String> compositeKey : possibleCompositeKeys) {
            Map<String, JSONObject> keyMap = new HashMap<>();
            JSONObject fake_a = objects_a.stream()
                    .collect(toJsonObject((JSONObject obj) -> {
                        String _key = extractKey(compositeKey, obj);
                        keyMap.putIfAbsent(_key, extractKeyValues(compositeKey, obj));
                        return _key;
                    }, Function.identity()));
            JSONObject fake_b = objects_b.stream()
                    .collect(toJsonObject((JSONObject obj) -> {
                        String _key = extractKey(compositeKey, obj);
                        keyMap.putIfAbsent(_key, extractKeyValues(compositeKey, obj));
                        return _key;
                    }, Function.identity()));

            Triple<JSONObject, JSONObject, JSONObject> partition = partition(fake_a, fake_b);
            double score = gradeObjectPartition(compositeKey, partition);

            JSONUtils.<JSONObject>stream(partition.getLeft()).forEach(e -> addAll(e.getValue(), keyMap.get(e.getLeft())));
            JSONUtils.<JSONObject>stream(partition.getRight()).forEach(e -> addAll(e.getValue(), keyMap.get(e.getLeft())));

            if (score > 0
                    && (bestPartition == null
                        || bestScore < score)) {
                bestKey = compositeKey;
                bestScore = score;
                bestPartition = partition;
            }
        }

        return bestPartition == null
                ? null
                : Pair.of(bestKey, bestPartition);
    }

    private static double gradeObjectPartition(@Nonnull List<String> compositeKey, @Nonnull Triple<JSONObject, JSONObject, JSONObject> partition) {
        int total = partition.getLeft().length() + partition.getMiddle().length() + partition.getRight().length();
        return total == 0
                ? 0
                : ((double) partition.getMiddle().length() / (double) total);
    }

    private static boolean canIndexOverKey(@Nonnull List<String> keys, @Nonnull List<JSONObject> items) {
        return items.stream()
                .map(obj -> keys.stream().map(obj::get).collect(Collectors.toList()))
                .filter(key -> key.stream().allMatch(v -> v == null
                        || v instanceof Integer
                        || v instanceof Long
                        || v instanceof String
                        || (v instanceof JSONArray && stream(v).allMatch(v2 -> v2 == null
                                || v2 instanceof Integer
                                || v2 instanceof Long
                                || v2 instanceof String))))
                .map(JSONUtils::toKeyString)
                .collect(Collectors.toSet())
                .size() == items.size();
    }

    private static String extractKey(@Nonnull List<String> keys, @Nonnull JSONObject item) {
        return keys.size() == 1
                ? toKeyString(item.get(keys.get(0)))
                : keys.stream()
                    .map(item::get)
                    .map(JSONUtils::toKeyString)
                    .collect(Collectors.joining(",", "[", "]"));
    }

    private static JSONObject extractKeyValues(@Nonnull List<String> keys, @Nonnull JSONObject item) {
        return keys.stream()
                .collect(toJsonObject(Function.identity(), item::get));
    }

    public static boolean compatibleJsonValues(@Nonnull Object a, @Nonnull Object b) {
        return normalizeToString(a).equals(normalizeToString(b));
    }

    @Nonnull
    public static String normalizeToString(@Nullable Object v) {
        return v instanceof Boolean
                ? (((Boolean) v) ? "1" : "0")
                : v instanceof String
                    ? (StringUtils.isFloat((String) v)
                        ? new DecimalFormat("0.000000").format(Double.parseDouble((String)v))
                        : (String) v)
                    : (v instanceof Number
                        ? new DecimalFormat("0.000000").format(v)
                        : (v instanceof JSONArray
                            ? stream(v).map(JSONUtils::normalizeToString).collect(Collectors.toList()).toString()
                            : String.valueOf(v)));
    }

    @Nonnull
    public static String toKeyString(@Nullable Object v) {
        boolean isArray = v instanceof JSONArray;
        return isArray || v instanceof Collection
                ? (isArray ? stream(v) : ((Collection) v).stream()).map(JSONUtils::toKeyString).collect(Collectors.toList()).toString()
                : String.valueOf(v);
    }

    public static int hashcode(@Nonnull JSONObject o) {
        int hashcode = 1;
        for (String key : o.keySet()) {
            hashcode = 31 * hashcode + key.hashCode();

            Object obj = o.get(key);
            hashcode = 31 * hashcode + HashcodeUtils.hashcode(obj, JSONUtils::toJSONSafeObject);
        }
        return hashcode;
    }

    public static int hashcode(@Nonnull JSONArray a) {
        int hashcode = 1, n = a.length();
        for (int i = 0; i < n; i++) {
            Object o = a.get(i);
            hashcode = 31 * hashcode + HashcodeUtils.hashcode(o, JSONUtils::toJSONSafeObject);
        }
        return hashcode;
    }

    @Nonnull
    public static <T> Collector<T, ?, JSONObject> toJsonObject(@Nonnull Function<T, String> keyFunction, @Nonnull Function<T, ?> valueFunction) {
        return Collector.of(JSONObject::new,
                (json, m) -> json.put(keyFunction.apply(m), valueFunction.apply(m)),
                (a, b) -> { addAll(a, b); return a; });
    }

    @Nonnull
    public static <T> Collector<T, ?, JSONArray> toJsonArray() {
        return Collector.of(JSONArray::new,
                (a, v) -> a.put(toJSONSafeObject(v)),
                (a, b) -> { stream(b).forEach(a::put); return a; });
    }

    @Nonnull
    public static <C, I> JSONArray toJsonArray(@Nonnull final C collection,
                                               @Nonnull final Function<C, Integer> lengthFunction,
                                               @Nonnull final BiFunction<C, Integer, I> arrayAccessFunction) {
        final int n = lengthFunction.apply(collection);
        final JSONArray out = new JSONArray(n);
        for (int i=0; i<n; i++) {
            out.put(toJSONSafeObject(arrayAccessFunction.apply(collection, i)));
        }
        return out;
    }

    @Nonnull
    public static <I, C extends Iterable<I>> JSONArray toJsonArray(@Nonnull final C iterable) {
        return toJsonArray(iterable, Iterable::iterator);
    }

    @Nonnull
    public static <I> JSONArray toJsonArray(@Nonnull final I[] array) {
        return toJsonArray(array, arr -> {
            final int n = arr.length;

            return new Iterator<I>() {
                private int c = 0;
                @Override
                public boolean hasNext() {
                    return c < n;
                }
                @Override
                public I next() {
                    return arr[c++];
                }
            };
        });
    }

    @Nonnull
    public static <C, I> JSONArray toJsonArray(@Nonnull final C iterable,
                                               @Nonnull final Function<C, Iterator<I>> iteratorFactory) {
        final Iterator<I> iterator = iteratorFactory.apply(iterable);
        final JSONArray out = new JSONArray();
        while (iterator.hasNext()) {
            out.put(toJSONSafeObject(iterator.next()));
        }
        return out;
    }

    @Nonnull
    public static JSONObject toJsonObject(@Nonnull Throwable t) {
        JSONObject out = new JSONObject()
                .put("_time", GregorianCalendar.getInstance().getTime().toString())
                .put("_class", t.getClass().getName())
                .put("_message", Optional.ofNullable(t.getMessage()).orElse("NULL"));

        out.put("trace", Arrays.stream(t.getStackTrace())
                .map(ele -> new JSONObject()
                        .put("_class", ele.getClassName())
                        .put("_method", ele.getMethodName())
                        .putOpt("_file", ele.getFileName())
                        .put("_line", ele.getLineNumber())
                        .put("_native", ele.isNativeMethod())
                        .put("_to-string", ele.toString()))
                .collect(Collectors.toList()));

        if (t.getCause() != null && t.getCause() != t) {
            out.put("_cause", toJsonObject(t.getCause()));
        }
        if (t.getSuppressed().length > 0) {
            out.put("_suppressed", Arrays.stream(t.getSuppressed()).map(JSONUtils::toJsonObject).collect(Collectors.toList()));
        }

        return out;
    }

    @Nonnull
    public static Object toJSONSafeObject(@Nullable final Object o) {
        if (o == null || JSONObject.NULL.equals(o)) {
            return JSONObject.NULL;
        } else if (o instanceof JSONObject
                || o instanceof JSONArray
                || o.getClass().isPrimitive()
                || o instanceof Number
                || o instanceof Boolean
                || o instanceof Enum) {
            return o;
        } else if (o instanceof Character
                || o instanceof Class
                || o instanceof UUID
                || o instanceof File
                || o instanceof Path
                || o instanceof java.net.URI
                || o instanceof org.eclipse.rdf4j.model.URI) {
            return o.toString();
        } else if (o instanceof CharSequence) {
            return o.toString()
                    .replaceAll("(?<!\\\\)\\R", "\\n");
        } else if (o instanceof IHasJSON) {
            return ((IHasJSON) o).toJSON();
        } else if (o instanceof Throwable) {
            return toJsonObject((Throwable) o);
        } else if (o instanceof Collection) {
            return ((Collection<?>) o).stream().collect(toJsonArray());
        } else if (o instanceof Stream) {
            return ((Stream<?>) o).collect(toJsonArray());
        } else if (o.getClass().isArray()) {
            return toJsonArray(o, Array::getLength, Array::get);
        } else if (o instanceof Map) {
            return ((Map<?, ?>) o).entrySet()
                    .stream()
                    .collect(toJsonObject(e -> String.valueOf(e.getKey()), e -> toJSONSafeObject(e.getValue())));
        } else if (o instanceof Optional) {
            return new JSONObject()
                    .put("_optional-value", ((Optional) o).isPresent()
                        ? toJSONSafeObject(((Optional) o).get())
                        : JSONObject.NULL);
        } else if (o.getClass().getPackage().toString().equals("java.util.concurrent.atomic")) {
            if (o instanceof AtomicReference) {
                return toJSONSafeObject(((AtomicReference) o).get());
            } else if (o instanceof AtomicMarkableReference) {
                return toJSONSafeObject(((AtomicMarkableReference) o).getReference());
            } else if (o instanceof AtomicStampedReference) {
                return toJSONSafeObject(((AtomicStampedReference) o).getReference());
            } else if (o instanceof AtomicIntegerArray) {
                return toJsonArray((AtomicIntegerArray) o, AtomicIntegerArray::length, AtomicIntegerArray::get);
            } else if (o instanceof AtomicLongArray) {
                return toJsonArray((AtomicLongArray) o, AtomicLongArray::length, AtomicLongArray::get);
            } else if (o instanceof AtomicReferenceArray) {
                return toJsonArray((AtomicReferenceArray) o, AtomicReferenceArray::length, AtomicReferenceArray::get);
            } else {
                return o.toString();
            }
        } else if (o instanceof LazyObject) {
            return toJSONSafeObject(((LazyObject) o).get());
        } else if (o instanceof Pair) {
            String mutablePrefix = o instanceof MutablePair ? "" : "_";
            return new JSONObject()
                    .put(mutablePrefix + "left", toJSONSafeObject(((Pair) o).getLeft()))
                    .put(mutablePrefix + "right", toJSONSafeObject(((Pair) o).getRight()));
        } else if (o instanceof Map.Entry) {
            return new JSONObject()
                    .put("_key", toJSONSafeObject(((Map.Entry) o).getKey()))
                    .put("value", toJSONSafeObject(((Map.Entry) o).getValue()));
        } else if (o instanceof Triple) {
            String mutablePrefix = o instanceof MutableTriple ? "" : "_";
            return new JSONObject()
                    .put(mutablePrefix + "left", toJSONSafeObject(((Triple) o).getLeft()))
                    .put(mutablePrefix + "middle", toJSONSafeObject(((Triple) o).getMiddle()))
                    .put(mutablePrefix + "right", toJSONSafeObject(((Triple) o).getRight()));
        } else if (o instanceof Multimap) {
            return ((Multimap<?, ?>) o).asMap()
                    .entrySet()
                    .stream()
                    .collect(toJsonObject(e -> String.valueOf(e.getKey()), e -> toJSONSafeObject(e.getValue())));
        } else if (o instanceof Enumeration) {
            return toJsonArray((Enumeration) o, e -> new Iterator<Object>() {
                @Override public boolean hasNext() {
                    return e.hasMoreElements();
                }
                @Override public Object next() {
                    return e.nextElement();
                }});
        } else if (o instanceof Iterable) {
            return toJsonArray((Iterable<?>) o);
        } else if (o instanceof Date
                || o instanceof Calendar
                || o instanceof Temporal
                || o instanceof XMLGregorianCalendar) {
            return new JSONArray()
                    .put(TimeUtils.toMillis(o))
                    .put(Literal.create(o).stringValue());
        } else if (o instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) o;
            final JSONObject json = new JSONObject()
                    .put("_class", tpe.getClass().getName())
                    .put("_pool-size", tpe.getPoolSize())
                    .put("_core-pool-size", tpe.getCorePoolSize())
                    .put("_largest-pool-size", tpe.getLargestPoolSize())
                    .put("_active-thread-count", tpe.getActiveCount())
                    .put("_queued-task-count", tpe.getQueue().size())
                    .put("_total-task-count", tpe.getTaskCount())
                    .put("_completed-task-count", tpe.getCompletedTaskCount())
                    .put("_status", tpe.isTerminated()
                            ? "Terminated" : tpe.isTerminating()
                            ? "Terminating" : tpe.isShutdown()
                            ? "Shutting down"
                            : "Running");

            if (o instanceof ScheduledThreadPoolExecutor) {
                ScheduledThreadPoolExecutor ste = (ScheduledThreadPoolExecutor) o;
                json.put("_remove-on-cancel-policy", ste.getRemoveOnCancelPolicy());
            }

            return json;
        }

        for (Extension extension : TO_JSON_EXTENSIONS) {
            if (extension.check(o)) {
                return extension.toJSONSafeObject(o);
            }
        }

        // currently just a trivial default case for anything outside of our domain
        return new JSONObject()
                .put("_class", o.getClass().getName())
                .put("_to-string", toJSONSafeObject(o.toString()));
    }

    public static void defineExtension(@Nonnull final Predicate<Object> check,
                                       @Nonnull final Function<Object, Object> toJSON) {
        new Extension() {
            @Override
            public boolean check(@Nonnull final Object o) {
                return check.test(o);
            }

            @Override
            @Nonnull
            public Object toJSONSafeObject(@Nonnull final Object o) {
                return toJSON.apply(o);
            }
        };
    }

    public static abstract class Extension {
        protected Extension() {
            TO_JSON_EXTENSIONS.add(this);
        }

        public abstract boolean check(@Nonnull final Object o);

        @Nonnull
        public abstract Object toJSONSafeObject(@Nonnull final Object o);

    }

}
