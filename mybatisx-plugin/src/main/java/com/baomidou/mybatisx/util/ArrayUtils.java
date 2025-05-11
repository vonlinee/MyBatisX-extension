package com.baomidou.mybatisx.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type Array utils.
 *
 * @author yanglin
 */
public final class ArrayUtils {

  private ArrayUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Gets only element.
   *
   * @param <T>      the type parameter
   * @param target   the target
   * @param defValue the def value
   * @return the only element
   */
  public static <T> Optional<T> getOnlyElement(@Nullable T[] target, @NotNull T defValue) {
    return Optional.of(getOnlyElement(target).orElse(defValue));
  }

  /**
   * Gets only element.
   *
   * @param <T>    the type parameter
   * @param target the target
   * @return the only element
   */
  public static <T> Optional<T> getOnlyElement(@Nullable T[] target) {
    return (null == target || 1 != target.length) ? Optional.<T>empty() : Optional.ofNullable(target[0]);
  }

  /**
   * 判断数组是否为空
   *
   * @param array 数组
   * @param <T>   数组元素类型
   * @return 数组是否为空
   */
  public static <T> boolean isEmpty(T[] array) {
    return array == null || array.length == 0;
  }

  /**
   * 将数组元素映射为新的数组类型
   *
   * @param array  原数组
   * @param mapper 映射逻辑
   * @param <E>    原数组元素类型
   * @param <T>    新数组元素类型
   * @return 新数组
   */
  public static <E, T> T[] map(@NotNull E[] array, @NotNull Function<E, T> mapper) {
    @SuppressWarnings("unchecked")
    T[] arr = (T[]) new Object[array.length];
    for (int i = 0; i < array.length; i++) {
      arr[i] = mapper.apply(array[i]);
    }
    return arr;
  }

  public static <R, T> List<R> asList(T[] array, Function<T, R> mapper) {
    return Arrays.stream(array).map(mapper).collect(Collectors.toList());
  }
}
