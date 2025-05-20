package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.feat.mybatis.DefaultMappedStatementSqlBuilder;
import com.baomidou.mybatisx.feat.mybatis.MappedStatementSqlBuilder;
import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.plugin.intention.DefaultMappedStatementParamGetter;
import com.baomidou.mybatisx.plugin.intention.MappedStatementParamGetter;
import com.baomidou.mybatisx.plugin.intention.MapperStatementEditor;
import com.baomidou.mybatisx.plugin.intention.MapperStatementParamTablePane;
import com.baomidou.mybatisx.plugin.intention.ParamImportPane;
import com.baomidou.mybatisx.plugin.intention.ParamNode;
import com.baomidou.mybatisx.plugin.intention.ResultSqlEditor;
import com.baomidou.mybatisx.util.SqlUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.JBSplitter;
import com.intellij.util.ui.JBUI;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.session.Configuration;
import org.mybatisx.extension.agent.mybatis.DynamicMyBatisConfiguration;
import org.mybatisx.extension.agent.mybatis.MapperStatementParser;
import org.mybatisx.extension.agent.mybatis.MissingCompatiableStatementBuilder;
import org.mybatisx.extension.agent.mybatis.MyMapperBuilderAssistant;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlPreviewPanel extends JPanel {

  MapperStatementEditor statementEditor;
  ResultSqlEditor resultSqlEditor;
  MapperStatementParamTablePane table;
  JBSplitter editorContainer;
  DynamicMyBatisConfiguration configuration;
  MyMapperBuilderAssistant assistant;
  MapperStatementParser msParser = new MapperStatementParser();
  JBSplitter center;
  Project project;
  ParamImportPane importPane;
  JBSplitter paramContainer;
  private XmlTag mapperStatementXmlTag;
  private String namespace;

  public SqlPreviewPanel(Project project) {
    super(new BorderLayout());
    this.setBorder(JBUI.Borders.empty(5, 10, 7, 10));

    this.project = project;
    this.initPanel(project, namespace);

    this.configuration = new DynamicMyBatisConfiguration(new Configuration());
    this.assistant = new MyMapperBuilderAssistant(this.configuration, null);
  }

  public void setMapperStatement(String namespace, XmlTag element) {
    this.namespace = namespace;
    this.mapperStatementXmlTag = (XmlTag) element.copy();
    // handle sql include
    statementEditor.updateStatement(element);
  }

  /**
   * 将字符串的statement解析为MappedStatement对象
   *
   * @param statement xml 包含<select/> <delete/> <update/> <insert/> 等标签
   * @return MappedStatement实例
   */
  public MappedStatement parseMappedStatement(String statement) {
    MissingCompatiableStatementBuilder statementParser = new MissingCompatiableStatementBuilder(configuration, msParser.getNode(statement), assistant);
    /**
     * 解析结果会放到 Configuration里
     * @see DynamicMyBatisConfiguration#addMappedStatement(MappedStatement)
     */
    return statementParser.parseMappedStatement();
  }

  /**
   * 结合参数获取实际的sql
   *
   * @return 可执行的sql
   */
  public String getSql() {
    String mapperStatement = statementEditor.getText();
    String sql = null;
    if (StringUtils.hasText(mapperStatement)) {
      Map<String, Object> map = table.getParamsAsMap();
      try {
        MappedStatement mappedStatement = parseMappedStatement(mapperStatement);
        MappedStatementSqlBuilder mappedStatementSqlBuilder = new DefaultMappedStatementSqlBuilder();
        sql = mappedStatementSqlBuilder.build(mappedStatement, map);
      } catch (Exception exception) {
        StringWriter writer = new StringWriter();
        try (PrintWriter pw = new PrintWriter(writer)) {
          exception.printStackTrace(pw);
        } finally {
          sql = writer.toString();
        }
      }
    }
    return sql;
  }

  public void fillSqlWithParams() {
    fillSqlWithParams(false);
  }

  public void fillSqlWithParams(boolean refreshParams) {
    if (refreshParams) {
      fillMapperStatementParams();
    }
    resultSqlEditor.setText(SqlUtils.format(getSql()));
  }

  private void initPanel(Project project, String namespace) {
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
    table = new MapperStatementParamTablePane();
    paramContainer.setFirstComponent(table);

    importPane = new ParamImportPane(project, table);
    paramContainer.setSecondComponent(importPane);
    center.setSecondComponent(paramContainer);

    this.add(center, BorderLayout.CENTER);

    // 弹窗根容器的宽度和高度
    // 使用 setSize 设置无效
    this.setPreferredSize(SwingUtils.getScreenBasedDimension(0.7));
  }

  public void fillMapperStatementParams() {
    Map<String, ParamDataType> paramMap = new HashMap<>();
    fillParams(mapperStatementXmlTag, paramMap);
    List<ParamNode> paramNodeList = new ArrayList<>();
    for (Map.Entry<String, ParamDataType> entry : paramMap.entrySet()) {
      paramNodeList.add(new ParamNode(entry.getKey(), null, entry.getValue()));
    }
    table.setAll(paramNodeList);
  }

  public void fillParams(PsiElement element, Map<String, ParamDataType> paramNodeMap) {
    MappedStatementParamGetter getter = new DefaultMappedStatementParamGetter();
    getter.getParams(element, paramNodeMap);
  }
}
