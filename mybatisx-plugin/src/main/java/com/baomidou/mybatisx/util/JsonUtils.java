package com.baomidou.mybatisx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.intellij.util.ExceptionUtil;
import net.minidev.json.JSONStyle;
import net.minidev.json.reader.JsonWriter;
import net.minidev.json.reader.JsonWriterI;

import java.io.IOException;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawuse"})
public abstract class JsonUtils {

  static final ObjectMapper objectMapper = new ObjectMapper();
  static final Gson gson = new Gson();

  public static String toJsonString(Map<?, ?> map) {
    String json;
    try {
      json = objectMapper.writeValueAsString(map);
    } catch (Throwable throwable) {
      json = ExceptionUtil.getThrowableText(throwable);
    }
    return json;
  }

  public static String toJsonPrettyString(Map<?, ?> map) {
    String json;
    try {
      json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    } catch (Throwable throwable) {
      json = ObjectUtils.toString(throwable);
    }
    return json;
  }

  public static String toJSONString(Object obj) {
    if (obj == null) {
      return "{}";
    }
    if (obj instanceof String) {
      return obj.toString();
    }
    StringBuilder res = new StringBuilder();
    JsonWriterI writer;
    if (obj instanceof Map) {
      writer = JsonWriter.JSONMapWriter;
    } else if (obj.getClass().isArray()) {
      writer = JsonWriter.arrayWriter;
    } else {
      writer = JsonWriter.beansWriter;
    }
    try {
      writer.writeJSONString(obj, res, JSONStyle.NO_COMPRESS);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return "";
  }

  public static JsonElement parseJsonTree(String json) {
    return gson.fromJson(json, JsonElement.class);
  }
}
