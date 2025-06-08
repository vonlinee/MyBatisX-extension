package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.component.BorderPane;
import com.baomidou.mybatisx.plugin.component.HBox;
import com.baomidou.mybatisx.plugin.component.Label;
import com.baomidou.mybatisx.plugin.intention.MapperStatementEditor;
import com.baomidou.mybatisx.plugin.intention.MapperStatementParamTablePane;
import com.baomidou.mybatisx.plugin.intention.ParamImportPane;
import com.baomidou.mybatisx.plugin.intention.ParamNode;
import com.baomidou.mybatisx.plugin.intention.ResultSqlEditor;
import com.baomidou.mybatisx.plugin.ui.UIHelper;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.ObjectUtils;
import com.baomidou.mybatisx.util.SqlUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.JBSplitter;
import org.apache.ibatis.mapping.ParameterMapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SqlPreviewPanel extends BorderPane {

  MapperStatementEditor statementEditor;
  ResultSqlEditor resultSqlEditor;
  MapperStatementParamTablePane table;
  JBSplitter editorContainer;
  JBSplitter center;
  Project project;
  ParamImportPane importPane;
  JBSplitter paramContainer;
  private String namespace;
  private Label label;

  public SqlPreviewPanel(Project project) {
    UIHelper.setEmptyBorder(this, 5, 10, 7, 10);
    this.project = project;
    this.initPanel(project, namespace);
  }

  public void setMapperStatement(String namespace, XmlTag element) {
    this.namespace = namespace;
    // handle sql include
    this.label.setText(namespace);
    statementEditor.setNamespace(namespace);
    statementEditor.updateStatement(element);
    statementEditor.setCaretPosition(0);
  }

  public void fillSqlWithParams() {
    fillSqlWithParams(false);
  }

  public void fillSqlWithParams(boolean refreshParams) {
    if (refreshParams) {
      fillMapperStatementParams();
    }
    Map<String, Object> map = table.getParamsAsMap();
    map = CollectionUtils.expandKeys(map, "\\.");

    try {
      String sql = statementEditor.computeSql(map);
      resultSqlEditor.setText(SqlUtils.format(sql));
    } catch (Throwable throwable) {
      resultSqlEditor.setText(ObjectUtils.toString(throwable));
    }
  }

  public void fillMapperStatementParams() {
    List<ParameterMapping> parameterMappings = statementEditor.getParameterMappings(this.namespace);
    if (parameterMappings.isEmpty()) {
      return;
    }
    // 去重
    Map<String, ParameterMapping> map = new HashMap<>();
    for (ParameterMapping parameterMapping : parameterMappings) {
      map.put(parameterMapping.getProperty(), parameterMapping);
    }
    parameterMappings = new ArrayList<>(map.values());
    ParamNode root = buildTree(parameterMappings);
    table.setAll(root.getChildren());
    importPane.generateParamTemplate(table.getParamsAsMap());
  }

  private static ParamNode buildTree(List<ParameterMapping> mappings) {
    ParamNode root = new ParamNode("root", null, ParamDataType.UNKNOWN); // 根节点
    for (ParameterMapping mapping : mappings) {
      String[] parts = mapping.getProperty().split("\\.");
      addToTree(root, parts, mapping);
    }
    return root;
  }

  private static void addToTree(ParamNode currentNode, String[] parts, ParameterMapping mapping) {
    for (String part : parts) {
      Optional<ParamNode> existingNode = Optional.ofNullable(currentNode.getChildren())
        .map(List::stream)
        .flatMap(stream -> stream.filter(child -> child.getKey().equals(part)).findFirst());
      if (existingNode.isPresent()) {
        currentNode = existingNode.get(); // 如果节点存在，进入该节点
      } else {
        ParamNode newNode = new ParamNode(part, null, getParamDataType(mapping));
        currentNode.addChild(newNode);
        currentNode = newNode; // 进入新节点
      }
    }
  }

  private static ParamDataType getParamDataType(ParameterMapping parameterMapping) {
    Class<?> javaType = parameterMapping.getJavaType();
    if (javaType == null) {
      return ParamDataType.STRING;
    } else if (javaType == Number.class) {
      return ParamDataType.NUMERIC;
    } else if (javaType == Date.class) {
      return ParamDataType.DATE;
    } else if (javaType == LocalDateTime.class) {
      return ParamDataType.TIME;
    } else if (javaType == Timestamp.class) {
      return ParamDataType.TIMESTAMP;
    } else if (javaType == Collection.class) {
      return ParamDataType.ARRAY;
    }
    return ParamDataType.STRING;
  }

  private void initPanel(Project project, String namespace) {
    HBox top = new HBox();
    Label label = new Label("Namespace: ");
    top.add(label);
    top.addSpacing(10);
    Label namespaceLabel = new Label(namespace == null ? "" : namespace);
    top.add(label);
    top.addSpacing(10);
    top.add(namespaceLabel);
    top.addSpacing(10);
    this.label = namespaceLabel;
    // SwingUtils.setPreferredWidth(this.label, 250);
    UIHelper.setEmptyBorder(top, 5);
    setTop(top);

    center = new JBSplitter(false, 0.5f);
    editorContainer = new JBSplitter(true, 0.5F);
    // Mapper Statement 编辑器
    statementEditor = new MapperStatementEditor(project);
    statementEditor.setPreferredWidth(500);

    editorContainer.setFirstComponent(statementEditor);

    // 结果sql编辑器
    resultSqlEditor = new ResultSqlEditor(project);
    resultSqlEditor.setPreferredWidth(500);

    editorContainer.setSecondComponent(resultSqlEditor);
    center.setFirstComponent(editorContainer);
    paramContainer = new JBSplitter(true, 0.5f);
    // 参数表区域
    table = new MapperStatementParamTablePane(project);
    paramContainer.setFirstComponent(table);

    importPane = new ParamImportPane(project, table);
    paramContainer.setSecondComponent(importPane);
    center.setSecondComponent(paramContainer);

    setCenter(center);

    // 弹窗根容器的宽度和高度
    // 使用 setSize 设置无效

    this.setPreferredSize(SwingUtils.getScreenBasedDimension(0.7));
  }
}
