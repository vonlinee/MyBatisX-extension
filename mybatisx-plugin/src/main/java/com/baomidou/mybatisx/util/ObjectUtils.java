package com.baomidou.mybatisx.util;

import com.intellij.util.ExceptionUtil;

public final class ObjectUtils {

  private ObjectUtils() {
  }

  public static String toString(Object obj) {
    if (obj == null) {
      return "null";
    }
    return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
  }

  public static String toString(Throwable throwable) {
    return ExceptionUtil.getThrowableText(throwable);
  }
}
