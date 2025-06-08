package com.baomidou.mybatisx.util;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

  public static <E> List<E> modifiableList(@Nullable List<E> list) {
    if (list == null) {
      return new ArrayList<>();
    }
    if (list.getClass().getName().contains("Unmodifiable")) {
      return new ArrayList<>(list);
    }
    return list;
  }

  /**
   * @param data key扁平化的map，例如 {"user.name": "zs", "user.age" : 26, "sex": false}
   * @return 嵌套Map
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> expandKeys(Map<String, Object> data, String separator) {
    Map<String, Object> result = new LinkedHashMap<>();
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      String[] keys = entry.getKey().split("\\.");
      Object value = entry.getValue();
      Map<String, Object> map = result;
      for (int i = 0; i < keys.length - 1; i++) {
        String key = keys[i];
        if (!map.containsKey(key)) {
          map.put(key, new LinkedHashMap<>());
        }
        map = (Map<String, Object>) map.get(key);
      }
      map.put(keys[keys.length - 1], value);
    }
    return result;
  }

  public static <T> T getOnlyElement(Iterable<T> iterable) {
    return Iterables.getOnlyElement(iterable);
  }
}
