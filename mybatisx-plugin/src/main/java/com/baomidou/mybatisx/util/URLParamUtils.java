package com.baomidou.mybatisx.util;

import org.jetbrains.annotations.Nullable;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class URLParamUtils {

  // 数字模式匹配
  private static final Pattern NUMBER_PATTERN = Pattern.compile("^-?\\d+(\\.\\d+)?$");

  /**
   * 解析URL参数字符串为Map
   *
   * @param queryString URL参数字符串，如 "a=1&a=2&b=zs"
   * @return 包含解析后参数的Map，支持一个key对应多个值
   */
  public static Map<String, Object> parseUrlParams(String queryString) {
    Map<String, Object> result = new LinkedHashMap<>();

    if (queryString == null || queryString.trim().isEmpty()) {
      return result;
    }
    if (queryString.startsWith("http")) {
      int i = queryString.indexOf("?");
      if (i > 0) {
        queryString = queryString.substring(i);
      }
    }
    String[] pairs = queryString.split("&");
    for (String pair : pairs) {
      if (pair.isEmpty()) {
        continue;
      }

      String[] keyValue = pair.split("=", 2);
      String key = decodeUTF8(keyValue[0]);

      if (keyValue.length == 1) {
        // 只有key没有value的情况
        addToResult(result, key, null);
      } else {
        // 有key和value的情况
        String value = decodeUTF8(keyValue[1]);
        Object parsedValue = parseValue(value);
        addToResult(result, key, parsedValue);
      }
    }

    return result;
  }

  public static String buildUrl(String url, Map<String, Object> params) {
    String urlParamString = toUrlParamString(params);
    if (StringUtils.isEmpty(urlParamString)) {
      return url;
    }
    return url + "?" + urlParamString;
  }

  /**
   * 将Map转换为URL参数字符串
   *
   * @param params 参数Map
   * @return URL参数字符串
   */
  public static String toUrlParamString(@Nullable Map<String, Object> params) {
    if (CollectionUtils.isEmpty(params)) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String key = encodeUTF8(entry.getKey());
      Object value = entry.getValue();

      if (value instanceof List) {
        // 处理多个值的情况
        List<?> values = (List<?>) value;
        for (Object val : values) {
          if (!first) {
            sb.append("&");
          }
          sb.append(key).append("=").append(encodeValue(val));
          first = false;
        }
      } else {
        if (!first) {
          sb.append("&");
        }
        sb.append(key).append("=").append(encodeValue(value));
        first = false;
      }
    }

    return sb.toString();
  }

  /**
   * 向结果Map中添加键值对，处理一个key多个值的情况
   */
  private static void addToResult(Map<String, Object> result, String key, Object value) {
    if (result.containsKey(key)) {
      Object existingValue = result.get(key);
      if (existingValue instanceof List) {
        // 如果已经是列表，直接添加
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) existingValue;
        list.add(value);
      } else {
        // 如果不是列表，创建新列表并添加原有值和当前值
        List<Object> list = new ArrayList<>();
        list.add(existingValue);
        list.add(value);
        result.put(key, list);
      }
    } else {
      // 新key，直接放入
      result.put(key, value);
    }
  }

  /**
   * 解析字符串值为合适的Java对象
   */
  private static Object parseValue(String value) {
    if (value == null) {
      return null;
    }

    // 检查是否为数字
    if (NUMBER_PATTERN.matcher(value).matches()) {
      try {
        if (value.contains(".")) {
          return Double.parseDouble(value);
        } else {
          // 尝试解析为Long，如果太大则保持字符串
          long longValue = Long.parseLong(value);
          if (longValue > Integer.MAX_VALUE || longValue < Integer.MIN_VALUE) {
            return longValue;
          } else {
            return (int) longValue;
          }
        }
      } catch (NumberFormatException e) {
        // 解析失败，保持为字符串
        return value;
      }
    }

    // 检查布尔值
    if ("true".equalsIgnoreCase(value)) {
      return true;
    }
    if ("false".equalsIgnoreCase(value)) {
      return false;
    }

    // 默认返回字符串
    return value;
  }

  /**
   * URL解码
   */
  private static String decodeUTF8(String encoded) {
    return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
  }

  /**
   * URL编码
   */
  private static String encodeUTF8(String decoded) {
    return URLEncoder.encode(decoded, StandardCharsets.UTF_8);
  }

  /**
   * 编码值为字符串
   */
  private static String encodeValue(Object value) {
    if (value == null) {
      return "";
    }
    return encodeUTF8(value.toString());
  }

  public static void main(String[] args) {
    String queryString = "a=1&a=2&b=zs&c=hello%20world&d=3.14&e=true";
    System.out.println("原始URL参数: " + queryString);

    Map<String, Object> params = parseUrlParams(queryString);
    System.out.println("解析后的Map: " + params);

    // 测试类型推断
    System.out.println("参数a的类型: " + params.get("a").getClass().getSimpleName());
    System.out.println("参数d的类型: " + params.get("d").getClass().getSimpleName());
    System.out.println("参数e的类型: " + params.get("e").getClass().getSimpleName());

    // 测试逆过程
    String rebuilt = toUrlParamString(params);
    System.out.println("重建的URL参数: " + rebuilt);

    // 测试包含特殊字符的情况
    String testSpecial = "name=John%20Doe&city=New%20York";
    System.out.println("\n特殊字符测试:");
    System.out.println("原始: " + testSpecial);
    Map<String, Object> specialParams = parseUrlParams(testSpecial);
    System.out.println("解析: " + specialParams);
    System.out.println("重建: " + toUrlParamString(specialParams));

    // 测试空值和单个值的情况
    String testSingle = "a=1&b=&c=only";
    System.out.println("\n单值和空值测试:");
    System.out.println("原始: " + testSingle);
    Map<String, Object> singleParams = parseUrlParams(testSingle);
    System.out.println("解析: " + singleParams);
    System.out.println("重建: " + toUrlParamString(singleParams));
  }
}
