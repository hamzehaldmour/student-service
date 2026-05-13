package com.rakbank.studentservice.utils;

import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * for connecting recursive hash code implementations within our various utility classes
 *
 * @author Aldmour Hamzeh (hamzeh.aldmour@acabes.com)
 * @version 1.0, Mar 8, 2026 at 4:04:19 PM
 * @editor IDEA
 */
public class HashcodeUtils {

    public static int hashcode(@Nullable Object o) {
        return hashcode(o, Function.identity());
    }

    public static int hashcode(@Nullable Object o,
                               @Nonnull Function<Object, Object> fallbackTransformation) {
        if (o == null) {
            return 0;
        } else if (o instanceof Set) {
            return CollectionUtils.hashcode((Set) o);
        } else if (o instanceof Multimap) {
            return MultimapUtils.hashcode((Multimap) o);
        } else if (o instanceof Map) {
            return MapUtils.hashcode((Map) o);
        } else if (o instanceof JSONObject) {
            return JSONUtils.hashcode((JSONObject) o);
        } else if (o instanceof JSONArray) {
            return JSONUtils.hashcode((JSONArray) o);
        } else if (o instanceof Pair) {
            return hashcode(((Pair) o).getLeft(), fallbackTransformation)
                    ^ hashcode(((Pair) o).getRight(), fallbackTransformation);
        } else if (o instanceof Triple) {
            return hashcode(((Triple) o).getLeft(), fallbackTransformation)
                    ^ hashcode(((Triple) o).getMiddle(), fallbackTransformation)
                    ^ hashcode(((Triple) o).getRight(), fallbackTransformation);
        } else {
            return fallbackTransformation.apply(o).hashCode();
        }
    }

}
