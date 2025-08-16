package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.components.SimpleTextEditor;
import com.baomidou.mybatisx.plugin.components.TabPane;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.JsonUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.impl.JBTabsImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParamImportPane extends BorderPane {

  private final SimpleTextEditor jsonParamEditor;
  private final SimpleTextEditor urlParamEditor;
  private final TabPane tabbedPane;

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
    jsonParamEditor.setText(JsonUtils.toJsonPrettyString(paramValues));
  }

  public String getUserInput() {
    String text = null;
    int index = tabbedPane.getSelectedIndex();
    List<ParamNode> paramNodes = new ArrayList<>();
    switch (index) {
      case 0: // json
        text = jsonParamEditor.getText();
        break;
      case 1: // url
        text = urlParamEditor.getText();
        break;
    }
    return text;
  }

  /**
   * TODO
   *
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
          parent.add(new ParamNode(key, element.getAsString(), ParamDataType.ARRAY));
        } else {
          ParamNode paramNode = new ParamNode(entry.getKey(), null, ParamDataType.UNKNOWN);
          parent.addChild(paramNode);
          parseJsonParams(entry.getKey(), entry.getValue(), paramNode);
        }
      }
    }
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
