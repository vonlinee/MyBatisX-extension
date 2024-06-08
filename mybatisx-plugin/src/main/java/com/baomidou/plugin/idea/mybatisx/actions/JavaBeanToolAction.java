package com.baomidou.plugin.idea.mybatisx.actions;

import com.baomidou.plugin.idea.mybatisx.ui.FieldsTable;
import com.baomidou.plugin.idea.mybatisx.util.SwingUtils;
import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

/**
 * JavaBean 工具
 */
public final class JavaBeanToolAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        Project project = event.getProject();

        JavaBeanToolDialog dialog = new JavaBeanToolDialog(project);
        dialog.show();
    }

    static class JavaBeanToolDialog extends DialogWrapper {

        Project project;
        JPanel mainPanel;
        FieldsTable beanFieldsTable;
        JPanel bottomPanel;
        JBSplitter centerPanel;

        public JavaBeanToolDialog(@Nullable Project project) {
            super(project);
            this.project = project;
            // 设置为非模态窗口
            setModal(false);

            setTitle("JavaBean Tool");
            init();
        }

        @Override
        protected @Nullable JComponent createCenterPanel() {
            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new BorderLayout());

            JButton button = new JButton("Select JavaBean");

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
                            .createWithInnerClassesScopeChooser("Select a Class", GlobalSearchScope.allScope(project), null, null);
                    chooser.showDialog();
                    PsiClass selectedClass = chooser.getSelected();
                    if (selectedClass != null) {
                        PsiField[] fields = selectedClass.getFields();
                        for (PsiField field : fields) {
                            // 字段初始化表达式
                            PsiExpression initializer = field.getInitializer();
                            String text = null;
                            if (initializer != null) {
                                text = initializer.getText();
                            }

                            PsiType type = field.getType();
                            String typeText = type.getCanonicalText();
                            beanFieldsTable.addRow(field.getName(), text, typeText);
                        }
                    }
                }
            });

            JPanel topPanel = SwingUtils.newBoxLayoutPanel();

            topPanel.add(button);

            this.beanFieldsTable = new FieldsTable();


            centerPanel = new JBSplitter(false, 0.4f);
            centerPanel.setFirstComponent(beanFieldsTable);

            ToolPane toolPane = new ToolPane();
            // 字段信息转DDL
            toolPane.addToolPane("DDL", new DDLCreatorTool(this.project));

            centerPanel.setSecondComponent(toolPane);

            this.mainPanel.add(centerPanel, BorderLayout.CENTER);
            this.mainPanel.add(topPanel, BorderLayout.NORTH);

            this.mainPanel.setPreferredSize(SwingUtils.getScreenBasedDimension(0.5));
            return this.mainPanel;
        }

        /**
         * 该样式会展示在会话框的最下方的位置
         *
         * @return Swing 组件
         */
        @Override
        protected JComponent createSouthPanel() {
            if (bottomPanel == null) {
                bottomPanel = new JPanel();
                BoxLayout layout = new BoxLayout(bottomPanel, BoxLayout.X_AXIS);
                bottomPanel.setLayout(layout);

                JButton btnGenerate = new JButton("生成");

                btnGenerate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Vector rows = beanFieldsTable.getRows();
                        System.out.println(rows);
                    }
                });

                bottomPanel.add(btnGenerate);
            }
            return bottomPanel;
        }

        @Override
        protected @NotNull JPanel createButtonsPanel(@NotNull List<? extends JButton> buttons) {
            return super.createButtonsPanel(buttons);
        }
    }

    static class ToolPane extends JTabbedPane {

        public ToolPane() {
            this.setTabPlacement(JTabbedPane.TOP);
            this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        }

        public final void addToolPane(String label, JPanel panel) {
            addTab(label, panel);
        }
    }

    /**
     * 创建DDL
     */
    static class DDLCreatorTool extends JPanel {

        JLabel dbTypeLabel;
        ComboBox<String> dbTypeComboBox;

        JPanel topPanel;
        EditorTextField ddlEditor;

        public DDLCreatorTool(Project project) {
            dbTypeLabel = new JLabel("数据库类型");
            dbTypeComboBox = new ComboBox<>();
            dbTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"MySQL"}));

            topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            topPanel.add(dbTypeLabel);
            topPanel.add(dbTypeComboBox);

            ddlEditor = new EditorTextField(project, SqlFileType.INSTANCE);

            setLayout(new BorderLayout());
            add(topPanel, BorderLayout.NORTH);
            add(ddlEditor, BorderLayout.CENTER);
        }
    }
}
