package com.baomidou.mybatisx.plugin.resultmap;

import java.util.Locale;

final class NamingUtils {

  private NamingUtils() {
  }

  static String toPropertyName(String name) {
    if (name == null || name.isBlank()) {
      return "value";
    }
    String normalized = name.trim().replace('`', ' ').replace('"', ' ').trim();
    StringBuilder result = new StringBuilder();
    boolean upper = false;
    for (int i = 0; i < normalized.length(); i++) {
      char ch = normalized.charAt(i);
      if (ch == '_' || ch == '-' || Character.isWhitespace(ch)) {
        upper = result.length() > 0;
        continue;
      }
      if (upper) {
        result.append(Character.toUpperCase(ch));
        upper = false;
      } else if (result.length() == 0) {
        result.append(Character.toLowerCase(ch));
      } else {
        result.append(ch);
      }
    }
    return result.length() == 0 ? "value" : result.toString();
  }

  static String simpleName(String qualifiedName) {
    if (qualifiedName == null || qualifiedName.isBlank()) {
      return "";
    }
    int index = Math.max(qualifiedName.lastIndexOf('.'), qualifiedName.lastIndexOf('$'));
    return index < 0 ? qualifiedName : qualifiedName.substring(index + 1);
  }

  static String packageName(String qualifiedName) {
    if (qualifiedName == null || qualifiedName.isBlank()) {
      return "";
    }
    int index = qualifiedName.lastIndexOf('.');
    return index < 0 ? "" : qualifiedName.substring(0, index);
  }

  static String classNameFromId(String id) {
    String property = toPropertyName(id);
    if (property.isBlank()) {
      return "Result";
    }
    return Character.toUpperCase(property.charAt(0)) + property.substring(1) + "Result";
  }

  static String lowerFirst(String text) {
    return text == null || text.isBlank() ? text : text.substring(0, 1).toLowerCase(Locale.ROOT) + text.substring(1);
  }
}
