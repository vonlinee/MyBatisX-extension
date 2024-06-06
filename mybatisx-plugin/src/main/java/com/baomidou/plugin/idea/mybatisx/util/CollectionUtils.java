package com.baomidou.plugin.idea.mybatisx.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The type Collection utils.
 *
 * @author yanglin
 */
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Is empty boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return null == collection || collection.isEmpty();
    }

    /**
     * Is not empty boolean.
     *
     * @param collection the collection
     * @return the boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <K, T> Map<K, T> toMap(Collection<T> collection, Function<T, K> keyMapper) {
        Map<K, T> map = new HashMap<>();
        for (T element : collection) {
            K key = keyMapper.apply(element);
            map.put(key, element);
        }
        return map;
    }
}
