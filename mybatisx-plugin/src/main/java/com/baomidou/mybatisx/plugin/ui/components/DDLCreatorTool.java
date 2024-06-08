package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.feat.bean.BeanInfo;
import com.baomidou.mybatisx.feat.bean.Field;
import com.baomidou.mybatisx.util.DdlFormatUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 创建DDL
 */
public final class DDLCreatorTool extends JPanel implements BeanToolHandler {

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

    public String getText(String tableName, List<Field> fieldList) {
        return DdlFormatUtil.buildDdlScript(tableName, fieldList);
    }

    @Override
    public JComponent getRoot() {
        return this;
    }

    @Override
    public String getText() {
        return "DDL";
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void accept(BeanInfo bean) {
        String result = getText(bean.getName(), bean.getFields());
        ddlEditor.setText(result);
    }
}
