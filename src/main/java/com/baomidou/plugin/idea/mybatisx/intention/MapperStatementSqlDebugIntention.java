package com.baomidou.plugin.idea.mybatisx.intention;

import com.baomidou.plugin.idea.mybatisx.model.ParamDataType;
import com.baomidou.plugin.idea.mybatisx.mybatis.DefaultMappedStatementSqlBuilder;
import com.baomidou.plugin.idea.mybatisx.mybatis.DynamicMyBatisConfiguration;
import com.baomidou.plugin.idea.mybatisx.mybatis.MapperStatementParser;
import com.baomidou.plugin.idea.mybatisx.mybatis.MissingCompatiableStatementBuilder;
import com.baomidou.plugin.idea.mybatisx.mybatis.MyMapperBuilderAssistant;
import com.baomidou.plugin.idea.mybatisx.mybatis.MappedStatementSqlBuilder;
import com.baomidou.plugin.idea.mybatisx.util.DomUtils;
import com.baomidou.plugin.idea.mybatisx.util.SqlUtils;
import com.baomidou.plugin.idea.mybatisx.util.StringUtils;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.xml.IXmlLeafElementType;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlToken;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ui.JBUI;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

public class MapperStatementSqlDebugIntention extends PsiElementBaseIntentionAction {

    @Override
    public @IntentionName
    @NotNull String getText() {
        return "MyBatisX Debug SQL";
    }

    @Override
    public @NotNull
    @IntentionFamilyName String getFamilyName() {
        return getText();
    }

    /**
     * 执行 Alter Enter 时根据此返回值决定该 Action 是否出现在菜单中
     *
     * @param project    项目信息
     * @param editor     编辑器
     * @param psiElement 触发的文档元素
     * @return 是否可用
     */
    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) {
        if (psiElement instanceof XmlToken) {
            XmlToken xmlToken = (XmlToken) psiElement;
            // XmlFile
            XmlFile mapperXmlFile = (XmlFile) xmlToken.getContainingFile();
            XmlDocument document = mapperXmlFile.getDocument();
            XmlTag rootTag = null;
            if (document == null || (rootTag = document.getRootTag()) == null) {
                return false;
            }
            String name = rootTag.getName();
            if (!"mapper".equals(name)) {
                return false;
            }

            XmlAttribute namespace = rootTag.getAttribute("namespace");
            if (namespace == null) {
                return false;
            }

            IElementType tokenType = xmlToken.getTokenType();
            if (tokenType instanceof IXmlLeafElementType) {
                PsiElement parent = xmlToken.getParent();
                if (parent instanceof XmlTag) {
                    XmlTag tag = (XmlTag) parent;
                    return "select".equals(tag.getName()) || "update".equals(tag.getName()) || "insert".equals(tag.getName()) || "delete".equals(tag.getName());
                }
            }
            return false;
        }
        return false;
    }

    /**
     * @param project    项目信息
     * @param editor     编辑器
     * @param psiElement 即isAvailable中PsiElement参数
     * @throws IncorrectOperationException 异常
     */
    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement psiElement) throws IncorrectOperationException {
        if (!(psiElement instanceof XmlToken)) {
            return;
        }

        XmlToken xmlToken = (XmlToken) psiElement;

        XmlFile mapperXmlFile = (XmlFile) xmlToken.getContainingFile();
        XmlDocument document = mapperXmlFile.getDocument();
        if (document == null || document.getRootTag() == null) {
            return;
        }
        XmlAttribute namespace = document.getRootTag().getAttribute("namespace");
        if (namespace == null) {
            return;
        }
        String mapperNamespace = namespace.getValue();

        // select, update, insert, delete 标签
        XmlTag parent = (XmlTag) xmlToken.getParent();

//        String id = getXmlAttributeValue(parent, "id");
//        String resultType = getXmlAttributeValue(parent, "resultType");
//        String parameterType = getXmlAttributeValue(parent, "parameterType");
//        String resultMap = getXmlAttributeValue(parent, "resultMap");
//        // 整个标签的文本内容
//        String text = parent.getText();

        MappedStatementDebuggerDialog dialog = new MappedStatementDebuggerDialog(project, mapperNamespace, parent);
        dialog.show();
        dialog.initUI();
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    public static class MappedStatementDebuggerDialog extends DialogWrapper {

        private JPanel mainPanel;
        private final XmlTag mapperStatementXmlTag;
        private String namespace;

        MapperStatementEditor textField;
        ResultSqlEditor resultSqlEditor;
        MapperStatementParamTablePane table;
        JSplitPane editorContainer;
        // 线程安全
        DynamicMyBatisConfiguration configuration;
        MyMapperBuilderAssistant assistant;
        MapperStatementParser msParser = new MapperStatementParser();
        JSplitPane center;
        Project project;
        ParamImportPane importPane;

        JSplitPane paramContainer;

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
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = toolkit.getScreenSize();
            this.mainPanel.setPreferredSize(new Dimension((int) (screenSize.getWidth() * 0.75), (int) (screenSize.getHeight() * 0.75)));
        }

        /**
         * 弹窗显示后进行初始化操作
         */
        public final void initUI() {
            // 设置分隔线位置，左右各一半
            center.setDividerLocation(0.5D);
            editorContainer.setDividerLocation(0.5D);
            paramContainer.setDividerLocation(0.5D);
            ApplicationManager.getApplication().invokeLater(this::fillMapperStatementParams);
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

    /**
     * @see com.intellij.ui.LanguageTextField
     */
    static class MapperStatementEditor extends EditorTextField {

        public MapperStatementEditor(Project project) {
            super(project, XmlFileType.INSTANCE);
        }

        @Override
        protected @NotNull EditorEx createEditor() {
            EditorEx editor = super.createEditor();
            // 水平滚动条
            HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
            // 垂直滚动条
            editor.setVerticalScrollbarVisible(true);
            return editor;
        }
    }

    static class ResultSqlEditor extends EditorTextField {
        public ResultSqlEditor(Project project) {
            super(project, SqlFileType.INSTANCE);
        }

        @Override
        protected @NotNull EditorEx createEditor() {
            EditorEx editor = super.createEditor();
            // 水平滚动条
            HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
            editor.setOneLineMode(false);
            // 垂直滚动条
            editor.setVerticalScrollbarVisible(true);
            return editor;
        }
    }

    static class DialogBottomPanel extends JPanel {

        MappedStatementDebuggerDialog dialog;

        public DialogBottomPanel(MappedStatementDebuggerDialog dialog) {
            this.dialog = dialog;
            BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(layout);

            JButton btnParseParams = new JButton("获取参数");

            btnParseParams.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dialog.fillMapperStatementParams();
                }
            });

            JButton btnGetSql = new JButton("获取SQL");
            btnGetSql.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dialog.fillSqlWithParams();
                }
            });

            // 参数导入覆盖模式
            ComboBox<ImportModel> comboBox = new ComboBox<>();
            comboBox.setPreferredSize(new Dimension(100, (int) comboBox.getPreferredSize().getHeight()));
            DefaultComboBoxModel<ImportModel> model = new DefaultComboBoxModel<>();
            comboBox.setModel(model);
            for (ImportModel value : ImportModel.values()) {
                model.addElement(value);
            }
            DefaultListCellRenderer renderer = new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {
                    if (value instanceof ImportModel) {
                        ImportModel person = (ImportModel) value;
                        setText(person.getLabel());
                    } else {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    }
                    return this;
                }
            };
            comboBox.setRenderer(renderer);

            JButton btnImportParams = new JButton("参数导入");
            btnImportParams.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    dialog.importParams(comboBox.getItem());
                }
            });

            add(btnParseParams);
            add(btnGetSql);
            add(comboBox);
            add(btnImportParams);
        }
    }
}
