package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.components.BorderPane;
import com.baomidou.mybatisx.plugin.components.Button;
import com.baomidou.mybatisx.plugin.components.CheckBox;
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
import com.baomidou.mybatisx.util.JBComponents;
import com.baomidou.mybatisx.util.SqlUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.AnActionButton;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.PlatformIcons;
import lombok.Getter;
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

  static final String ROOT = "root";

  private final Tabs tabPane;
  private final MapperStatementEditor statementEditor;
  private final SqlEditor resultSqlEditor;
  private final MapperStatementParamTablePane table;
  private final Label label;
  private final ParamImportPane importPane;

  /**
   * true - 直接使用JSON转换的数据作为参数进行SQL预览， false - 通过参数表格组件提取的参数
   */
  @Getter
  private boolean useRawUserInputParams;

  private String namespace;

  public SqlPreviewPanel(Project project) {
    UIHelper.setEmptyBorder(this, 5, 10, 7, 10);

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
    SplitPane paramContainer = new SplitPane(true, 0.5f);

    ParamImportPane importPane = new ParamImportPane(project);
    Button btnApply = new Button("Apply", "fill param table with params parsed by user input", new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
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
    Button btnHide = new Button("Close", "close params import panel", new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        importPane.setVisible(false);
      }
    });
    Button btnGenerate = new Button("Generate", "generate default params with null value", new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        importPane.generateParamTemplate(table.getParamsAsMap());
      }
    });

    importPane.setBottom(new HBox(btnApply, btnGenerate, btnHide));

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
    }, new AnActionButton("Enable of Disable Parameter Table", Icons.STATUS_ENABLED) {
      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        useRawUserInputParams = !useRawUserInputParams;
        Presentation presentation = e.getPresentation();
        if (useRawUserInputParams) {
          presentation.setIcon(Icons.STATUS_DISABLED);
          presentation.setText("Parameter Table Is Disabled");
        } else {
          presentation.setIcon(Icons.STATUS_ENABLED);
          presentation.setText("Parameter Table Is Enabled");
        }
        repaint();
      }
    }};
    table = new MapperStatementParamTablePane(actions);
    paramContainer.setFirstComponent(table);
    paramContainer.setRightComponent(importPane);
    center.setLeftComponent(tabPane);
    center.setRightComponent(paramContainer);

    this.importPane = importPane;
    setCenter(center);
    this.setPreferredSize(SwingUtils.getScreenBasedDimension(0.7));
  }

  public void setMapperStatement(String namespace, XmlTag element) {
    this.namespace = namespace;
    this.label.setText(namespace);
    statementEditor.setNamespace(namespace);
    statementEditor.updateStatement(element);
    JBComponents.setCaretPositionToBegin(statementEditor);
  }

  public void fillSqlWithParams() {
    fillSqlWithParams(false, false);
  }

  public void fillSqlWithParams(boolean inline) {
    fillSqlWithParams(inline, false);
  }

  public void fillSqlWithParams(boolean inline, boolean formatSql) {
    String sql = computeSqlWithParams(inline, false, formatSql);
    resultSqlEditor.setText(sql);
    tabPane.selectTab(1);
  }

  public String computeSqlWithParams(boolean inline, boolean refreshParams, boolean formatSql) {
    if (refreshParams) {
      fillMapperStatementParams();
    }
    Map<String, Object> map;
    if (useRawUserInputParams) {
      map = importPane.getParamsAsMap();
    } else {
      map = table.getParamsAsMap();
      map = CollectionUtils.expandKeys(map, StringUtils.SPLITTER);
    }
    try {
      String sql = statementEditor.computeSql(map, inline);
      return formatSql ? SqlUtils.format(sql) : sql;
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
    ParamNode root = new ParamNode(ROOT, null, ParamDataType.UNKNOWN); // 根节点
    for (ParameterMapping mapping : mappings) {
      String[] parts = mapping.getProperty().split(StringUtils.SPLITTER);
      addToTree(root, parts, mapping);
    }
    return root;
  }

  private static void addToTree(ParamNode currentNode, String[] parts, ParameterMapping mapping) {
    for (String part : parts) {
      Optional<ParamNode> existingNode = Optional.ofNullable(currentNode.getChildren())
        .map(List::stream)
        .flatMap(stream -> stream.filter(child -> child.getKey().equals(part))
          .findFirst());
      if (existingNode.isPresent()) {
        currentNode = existingNode.get(); // 如果节点存在，进入该节点
      } else {
        ParamNode newNode = new ParamNode(part, null, getParamDataType(mapping));
        if (mapping.getJdbcType() != null) {
          newNode.setJdbcType(mapping.getJdbcType().name());
        } else if (mapping.getJdbcTypeName() != null) {
          newNode.setJdbcType(mapping.getJdbcTypeName());
        }
        currentNode.addChild(newNode);
        currentNode = newNode;
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
    CheckBox cboxFormatSql = new CheckBox("Format Sql");
    box.add(cboxFormatSql);
    box.add(Box.createHorizontalStrut(5));
    Button btnGetSql = new Button("Raw SQL");
    btnGetSql.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillSqlWithParams(true, cboxFormatSql.isSelected());
      }
    });

    Button btnGetPreparedSql = new Button("Prepared SQL");
    btnGetPreparedSql.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        panel.fillSqlWithParams(false, cboxFormatSql.isSelected());
      }
    });
    box.add(btnGetPreparedSql);
    box.add(btnGetSql);
    return box;
  }
}
