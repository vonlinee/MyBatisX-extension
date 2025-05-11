package com.baomidou.mybatisx.util;

public final class ObjectUtils {

  private ObjectUtils() {
  }

  public static String toString(Object obj) {
    if (obj == null) {
      return "null";
    }
    return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
  }
}
