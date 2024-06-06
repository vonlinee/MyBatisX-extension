package com.baomidou.plugin.idea.mybatisx.intention;

import com.baomidou.plugin.idea.mybatisx.model.ParamDataType;
import com.baomidou.plugin.idea.mybatisx.mybatis.DefaultMappedStatementSqlBuilder;
import com.baomidou.plugin.idea.mybatisx.mybatis.MappedStatementSqlBuilder;
import com.baomidou.plugin.idea.mybatisx.util.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ui.JBUI;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.jetbrains.annotations.Nullable;
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

public class MappedStatementDebuggerDialog extends DialogWrapper {

    private final XmlTag mapperStatementXmlTag;
    MapperStatementEditor textField;
    ResultSqlEditor resultSqlEditor;
    MapperStatementParamTablePane table;
    JSplitPane editorContainer;
    DynamicMyBatisConfiguration configuration;
    MyMapperBuilderAssistant assistant;
    MapperStatementParser msParser = new MapperStatementParser();
    JSplitPane center;
    Project project;
    ParamImportPane importPane;
    JSplitPane paramContainer;
    private JPanel mainPanel;
    private String namespace;

    public MappedStatementDebuggerDialog(@Nullable Project project, String namespace, XmlTag mapperStatementXmlTag) {
        super(project);
        this.project = project;
        this.mapperStatementXmlTag = mapperStatementXmlTag;
        setModal(false);
        this.initPanel(project, namespace);
        textField.setText(mapperStatementXmlTag.getText());
        this.configuration = new DynamicMyBatisConfiguration(new Configuration());
        this.assistant = new MyMapperBuilderAssistant(this.configuration, null);
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
        String mapperStatement = textField.getText();
        String sql = null;
        if (StringUtils.hasText(mapperStatement)) {
            Map<String, Object> map = table.getParamsAsMap();
            if (map != null && !map.isEmpty()) {
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
        }
        return sql;
    }

    public void fillSqlWithParams() {
        fillSqlWithParams(false);
    }

    /**
     * 导入参数
     *
     * @param importModel 导入模式
     */
    public void importParams(ImportModel importModel) {
        if (importModel == null) {
            importModel = ImportModel.MERGE;
        }
        List<ParamNode> params = importPane.getParams();
        Map<String, ParamNode> paramNodeMap = new HashMap<>();
        for (ParamNode param : params) {
            paramNodeMap.put(param.getKey(), param);
        }
        fillMapperStatementParams(paramNodeMap, importModel);
    }

    public void fillSqlWithParams(boolean refreshParams) {
        if (refreshParams) {
            fillMapperStatementParams();
        }
        resultSqlEditor.setText(SqlUtils.format(getSql()));
    }

    private void initPanel(Project project, String namespace) {
        this.setTitle("Mapped Statement Sql Debugger");
        setOKButtonText("Next");
        setCancelButtonText("Cancel");
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBorder(JBUI.Borders.empty(5, 10, 7, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); // 水平排列

        topPanel.add(new JLabel(namespace + "." + DomUtils.getAttributeValue(mapperStatementXmlTag, "id")));

        this.mainPanel.add(topPanel, BorderLayout.NORTH);

        center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        editorContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // Mapper Statement 编辑器
        textField = new MapperStatementEditor(project);
        textField.setOneLineMode(false);
        textField.setPreferredWidth(500);
        textField.setEnabled(true);
        editorContainer.setLeftComponent(textField);

        // 结果sql编辑器
        resultSqlEditor = new ResultSqlEditor(project);
        resultSqlEditor.setOneLineMode(false);
        resultSqlEditor.setPreferredWidth(500);
        resultSqlEditor.setEnabled(true);

        editorContainer.setLeftComponent(textField);
        editorContainer.setRightComponent(resultSqlEditor);
        editorContainer.setDividerLocation(0.5D);

        center.setLeftComponent(editorContainer);
        // 设置分隔线位置，左右各一半
        center.setDividerLocation(0.5D);

        paramContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // 参数表区域
        table = new MapperStatementParamTablePane();
        paramContainer.setLeftComponent(table);

        importPane = new ParamImportPane(project);
        paramContainer.setRightComponent(importPane);
        center.setRightComponent(paramContainer);

        this.mainPanel.add(center, BorderLayout.CENTER);
        init();
        // 弹窗根容器的宽度和高度
        // 使用 setSize 设置无效
        this.mainPanel.setPreferredSize(SwingUtils.getScreenBasedDimension(0.75));
    }

    /**
     * 弹窗显示后进行初始化操作
     */
    public final void initUI() {
        // 设置分隔线位置，左右各一半 需要在show之后设置才有效
        center.setDividerLocation(0.5D);
        editorContainer.setDividerLocation(0.5D);
        paramContainer.setDividerLocation(0.5D);

        IntellijSDK.invokeLater(this::fillMapperStatementParams);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel;
    }

    /**
     * 该样式会展示在会话框的最下方的位置
     *
     * @return Swing 组件
     */
    @Override
    protected JComponent createSouthPanel() {
        return new DialogBottomPanel(this);
    }

    /**
     * 已经限制了是 MyBatis XML Mapper 文件
     */
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

    /**
     * 填充参数
     *
     * @param params 参数列表，不包含嵌套形式
     * @param mode   操作类型，1-全部覆盖，2-仅追加，3-追加且覆盖，4-合并不覆盖
     */
    public void fillMapperStatementParams(Map<String, ParamNode> params, ImportModel mode) {
        table.addParams(params, mode);
    }
}
