package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.components.SimpleTextEditor;
import com.baomidou.mybatisx.plugin.components.TabPane;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.JsonUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.URLParamUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParamImportPane extends BorderPane {

  private final SimpleTextEditor jsonParamEditor;
  private final SimpleTextEditor urlParamEditor;
  private final TabPane tabbedPane;

  private static final int TAB_JSON = 0;
  private static final int TAB_URL = 1;

  public ParamImportPane(Project project) {
    tabbedPane = new TabPane();
    jsonParamEditor = new SimpleTextEditor(project, JsonFileType.INSTANCE);
    tabbedPane.addTab("JSON", jsonParamEditor);

    urlParamEditor = new SimpleTextEditor(project, PlainTextFileType.INSTANCE);
    tabbedPane.addTab("URL", urlParamEditor);
    setCenter(tabbedPane);
  }

  public void generateParamTemplate(Map<String, Object> paramValues) {
    paramValues = CollectionUtils.expandKeys(paramValues, "\\.");
    int index = tabbedPane.getSelectedIndex();
    if (index == TAB_JSON) {
      jsonParamEditor.setText(JsonUtils.toJsonPrettyString(paramValues));
    } else if (index == TAB_URL) {
      urlParamEditor.setText(URLParamUtils.toUrlParamString(paramValues));
    }
  }

  public String getUserInput() {
    String text = null;
    int index = tabbedPane.getSelectedIndex();
    switch (index) {
      case TAB_JSON: // json
        text = jsonParamEditor.getText();
        break;
      case TAB_URL: // url
        text = urlParamEditor.getText();
        break;
    }
    return text;
  }

  /**
   * @param parent 父节点
   */
  private void parseJsonParams(String key, JsonElement element, ParamNode parent) {
    if (element.isJsonObject()) {
      JsonObject jsonObject = element.getAsJsonObject();
      for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
        element = entry.getValue();
        key = entry.getKey();
        if (element.isJsonNull()) {
          parent.addChild(new ParamNode(key, "null", ParamDataType.UNKNOWN));
        } else if (element.isJsonPrimitive()) {
          JsonPrimitive primitive = element.getAsJsonPrimitive();
          if (primitive.isBoolean()) {
            parent.add(new ParamNode(key, element.getAsString(), ParamDataType.BOOLEAN));
          } else if (primitive.isNumber()) {
            parent.add(new ParamNode(key, element.getAsString(), ParamDataType.NUMERIC));
          } else if (primitive.isString()) {
            parent.add(new ParamNode(key, element.getAsString(), ParamDataType.STRING));
          }
        } else if (element.isJsonArray()) {
          JsonArray jsonArray = element.getAsJsonArray();
          if (jsonArray.isEmpty()) {
            parent.addChild(new ParamNode(key, "", ParamDataType.ARRAY));
          } else {
            JsonElement jsonElement = jsonArray.get(0);
            String value = String.valueOf(element);
            if (jsonElement.isJsonPrimitive()) {
              JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();
              if (primitive.isString()) {
                parent.addChild(new ParamNode(key, value, ParamDataType.STRING_ARRAY));
              } else if (primitive.isNumber()) {
                parent.addChild(new ParamNode(key, value, ParamDataType.NUMBER_ARRAY));
              }
            } else {
              // 对象类型
              parent.addChild(new ParamNode(key, JsonUtils.toJSONString(element), ParamDataType.JSON_ARRAY));
            }
          }
        } else {
          ParamNode paramNode = new ParamNode(entry.getKey(), null, ParamDataType.UNKNOWN);
          parent.addChild(paramNode);
          parseJsonParams(entry.getKey(), entry.getValue(), paramNode);
        }
      }
    }
  }

  public Map<String, Object> getParamsAsMap() {
    int index = tabbedPane.getSelectedIndex();
    Map<String, Object> result = new HashMap<>();
    String text;
    switch (index) {
      case 0: // json
        text = jsonParamEditor.getText();
        if (StringUtils.isBlank(text)) {
          return Collections.emptyMap();
        }
        result.putAll(JsonUtils.parseJsonToMap(text));
      case 1:
        text = urlParamEditor.getText();
        if (!StringUtils.isBlank(text)) {
          int i = text.indexOf("?");
          if (i >= 0) {
            String[] nvPairs = text.substring(i + 1).split("&");
            for (String nvPair : nvPairs) {
              int j = nvPair.indexOf("=");
              if (j >= 0) {
                result.put(nvPair.substring(0, j), nvPair.substring(j + 1));
              }
            }
          } else {
            String[] nvPairs = text.split("&");
            for (String nvPair : nvPairs) {
              int j = nvPair.indexOf("=");
              if (j >= 0) {
                result.put(nvPair.substring(0, j), nvPair.substring(j + 1));
              }
            }
          }
        }
        break;
    }
    return result;
  }

  /**
   * 获取解析得到的参数
   *
   * @return 参数列表
   */
  public List<ParamNode> getParams() {
    String text;
    int index = tabbedPane.getSelectedIndex();
    List<ParamNode> paramNodes = new ArrayList<>();
    switch (index) {
      case 0: // json
        text = jsonParamEditor.getText();
        if (StringUtils.isBlank(text)) {
          return paramNodes;
        }
        ParamNode root = new ParamNode();
        parseJsonParams(null, JsonUtils.parseJsonTree(text), root);
        paramNodes = root.getChildren();
        break;
      case 1: // url参数
        text = urlParamEditor.getText();
        if (!StringUtils.isBlank(text)) {
          int i = text.indexOf("?");
          if (i >= 0) {
            String[] nvPairs = text.substring(i + 1).split("&");
            for (String nvPair : nvPairs) {
              int j = nvPair.indexOf("=");
              if (j >= 0) {
                paramNodes.add(new ParamNode(nvPair.substring(0, j), nvPair.substring(j + 1), null));
              }
            }
          } else {
            String[] nvPairs = text.split("&");
            for (String nvPair : nvPairs) {
              int j = nvPair.indexOf("=");
              if (j >= 0) {
                paramNodes.add(new ParamNode(nvPair.substring(0, j), nvPair.substring(j + 1), null));
              }
            }
          }
        }
        break;
    }
    return paramNodes;
  }

  private void flatten(Map<String, Object> res, String key, Object value) {
    if (value instanceof Map) {
      @SuppressWarnings("unchecked") Map<String, Object> nestMap = (Map<String, Object>) value;
      for (Map.Entry<String, Object> entry : nestMap.entrySet()) {
        flatten(res, key + "." + entry.getKey(), entry.getValue());
      }
    } else {
      res.put(key, value);
    }
  }
}
