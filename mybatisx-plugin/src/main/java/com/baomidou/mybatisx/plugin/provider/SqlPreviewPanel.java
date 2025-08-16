package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.components.Button;
import com.baomidou.mybatisx.plugin.components.HBox;
import com.baomidou.mybatisx.plugin.components.Label;
import com.baomidou.mybatisx.plugin.components.SplitPane;
import com.baomidou.mybatisx.plugin.components.Tabs;
import com.baomidou.mybatisx.plugin.intention.MapperStatementEditor;
import com.baomidou.mybatisx.plugin.intention.MapperStatementParamTablePane;
import com.baomidou.mybatisx.plugin.intention.ParamImportPane;
import com.baomidou.mybatisx.plugin.intention.ParamNode;
import com.baomidou.mybatisx.plugin.intention.SqlEditor;
import com.baomidou.mybatisx.plugin.ui.UIHelper;
import com.baomidou.mybatisx.util.CollectionUtils;
import com.baomidou.mybatisx.util.Icons;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.SqlUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.AnActionButton;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.PlatformIcons;
import org.apache.ibatis.mapping.ParameterMapping;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

  private final Tabs tabPane;
  private final MapperStatementEditor statementEditor;
  private final SqlEditor resultSqlEditor;
  private final MapperStatementParamTablePane table;
  private final SplitPane center;
  private final Project project;
  private final SplitPane paramContainer;
  private final Label label;

  private String namespace;

  public SqlPreviewPanel(Project project) {
    UIHelper.setEmptyBorder(this, 5, 10, 7, 10);
    this.project = project;

    HBox top = new HBox();
    Label label = new Label("Namespace: ");
    Label namespaceLabel = new Label(namespace);
    top.addChildrenWithSpacing(10, label, namespaceLabel);

    this.label = namespaceLabel;
    UIHelper.setEmptyBorder(top, 5);
    setTop(top);

    SplitPane center = new SplitPane(false, 0.5f);
    // Mapper Statement 编辑器
    statementEditor = new MapperStatementEditor(project);
    // 结果sql编辑器
    resultSqlEditor = new SqlEditor(project);

    Tabs tabPane = new Tabs(project);
    tabPane.addTab("MappedStatement", statementEditor);
    tabPane.addTab("SQL", resultSqlEditor);

    center.setFirstComponent(this.tabPane = tabPane);
    paramContainer = new SplitPane(true, 0.5f);

    ParamImportPane importPane = new ParamImportPane(project);
    Button btnApply = new Button("Apply");
    btnApply.setToolTipText("fill param table with params parsed by user input");
    btnApply.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (!importPane.isVisible()) {
          importPane.setVisible(true);
        }
        List<ParamNode> params = importPane.getParams();
        if (params == null || params.isEmpty()) {
          return;
        }
        table.resetAll(params);
      }
    });
    Button btnHide = new Button("Close");
    btnHide.setToolTipText("close params import panel");
    btnHide.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        importPane.setVisible(false);
      }
    });
    Button btnGenerate = new Button("Generate");
    btnGenerate.setToolTipText("generate default params with null value");
    btnGenerate.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        importPane.generateParamTemplate(table.getParamsAsMap());
      }
    });
    HBox hBox = new HBox();
    hBox.addChildren(btnApply, btnGenerate, btnHide);
    importPane.setBottom(hBox);

    AnActionButton[] actions = new AnActionButton[]{new AnActionButton("Import Params", "Import params", PlatformIcons.IMPORT_ICON) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        if (!importPane.isVisible() && StringUtils.isBlank(importPane.getUserInput())) {
          IntellijSDK.invokeLater(() -> importPane.generateParamTemplate(table.getParamsAsMap()));
        }
        importPane.setVisible(true);
      }
    }, new AnActionButton("Refresh Params", Icons.AUTO_REFRESH) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        fillMapperStatementParams();
      }
    }};
    table = new MapperStatementParamTablePane(actions);

    importPane.setVisible(false);

    paramContainer.setFirstComponent(table);
    paramContainer.setRightComponent(importPane);
    center.setLeftComponent(tabPane);
    center.setRightComponent(paramContainer);

    setCenter(this.center = center);
    this.setPreferredSize(SwingUtils.getScreenBasedDimension(0.7));
  }

  public void setMapperStatement(String namespace, XmlTag element) {
    this.namespace = namespace;
    this.label.setText(namespace);
    statementEditor.setNamespace(namespace);
    statementEditor.updateStatement(element);
    statementEditor.setCaretPosition(0);
  }

  public void fillSqlWithParams() {
    fillSqlWithParams(false);
  }

  public void fillSqlWithParams(boolean inline) {
    String sql = computeSqlWithParams(inline, false);
    resultSqlEditor.setText(sql);
    tabPane.selectTab(1);
  }

  public String computeSqlWithParams(boolean inline, boolean refreshParams) {
    if (refreshParams) {
      fillMapperStatementParams();
    }
    Map<String, Object> map = table.getParamsAsMap();
    map = CollectionUtils.expandKeys(map, "\\.");
    try {
      String sql = statementEditor.computeSql(map, inline);
      return SqlUtils.format(sql);
    } catch (Throwable throwable) {
      return ExceptionUtil.getThrowableText(throwable);
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
      Optional<ParamNode> existingNode = Optional.ofNullable(currentNode.getChildren()).map(List::stream).flatMap(stream -> stream.filter(child -> child.getKey().equals(part)).findFirst());
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

  public static Box createOperationBox(SqlPreviewPanel panel) {
    Box box = Box.createHorizontalBox();
    box.add(Box.createHorizontalGlue());
    Button btnGetSql = new Button("Raw SQL");
    btnGetSql.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillSqlWithParams(true);
      }
    });

    Button btnGetPreparedSql = new Button("Prepared SQL");
    btnGetPreparedSql.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillSqlWithParams();
      }
    });
    box.add(btnGetPreparedSql);
    box.add(btnGetSql);
    return box;
  }
}
