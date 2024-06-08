package com.baomidou.mybatisx.plugin.intention;

import com.baomidou.mybatisx.model.ParamDataType;
import com.baomidou.mybatisx.mybatis.DefaultMappedStatementSqlBuilder;
import com.baomidou.mybatisx.mybatis.MappedStatementSqlBuilder;
import com.baomidou.mybatisx.util.DomUtils;
import com.baomidou.mybatisx.util.IntellijSDK;
import com.baomidou.mybatisx.util.SqlUtils;
import com.baomidou.mybatisx.util.StringUtils;
import com.baomidou.mybatisx.util.SwingUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.JBSplitter;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    JBSplitter editorContainer;
    DynamicMyBatisConfiguration configuration;
    MyMapperBuilderAssistant assistant;
    MapperStatementParser msParser = new MapperStatementParser();
    JBSplitter center;
    Project project;
    ParamImportPane importPane;
    JBSplitter paramContainer;
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
        this.setTitle("Mapped Statement Sql Debugger");
        setOKButtonText("Next");
        setCancelButtonText("Cancel");
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.setBorder(JBUI.Borders.empty(5, 10, 7, 10));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS)); // 水平排列

        topPanel.add(new JLabel(namespace + "." + DomUtils.getAttributeValue(mapperStatementXmlTag, "id")));

        this.mainPanel.add(topPanel, BorderLayout.NORTH);

        center = new JBSplitter(false, 0.5f);

        editorContainer = new JBSplitter(true, 0.5F);
        // Mapper Statement 编辑器
        textField = new MapperStatementEditor(project);
        textField.setOneLineMode(false);
        textField.setPreferredWidth(500);
        textField.setEnabled(true);
        editorContainer.setFirstComponent(textField);

        // 结果sql编辑器
        resultSqlEditor = new ResultSqlEditor(project);
        resultSqlEditor.setOneLineMode(false);
        resultSqlEditor.setPreferredWidth(500);
        resultSqlEditor.setEnabled(true);

        editorContainer.setSecondComponent(resultSqlEditor);

        center.setFirstComponent(editorContainer);

        paramContainer = new JBSplitter(true, 0.5f);
        // 参数表区域
        table = new MapperStatementParamTablePane();
        paramContainer.setFirstComponent(table);

        importPane = new ParamImportPane(project, table);
        paramContainer.setSecondComponent(importPane);
        center.setSecondComponent(paramContainer);

        this.mainPanel.add(center, BorderLayout.CENTER);
        init();
        // 弹窗根容器的宽度和高度
        // 使用 setSize 设置无效
        this.mainPanel.setPreferredSize(SwingUtils.getScreenBasedDimension(0.5));
    }

    /**
     * 弹窗显示后进行初始化操作
     */
    public final void initUI() {
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
        Box box = Box.createHorizontalBox();

        box.add(Box.createHorizontalGlue());

        JButton btnParseParams = new JButton("获取参数");
        btnParseParams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillMapperStatementParams();
            }
        });

        JButton btnGetSql = new JButton("获取SQL");
        btnGetSql.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillSqlWithParams();
            }
        });
        box.add(btnParseParams);
        box.add(btnGetSql);
        return box;
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
}
