package com.baomidou.plugin.idea.mybatisx.intention;

import com.intellij.openapi.ui.ComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialogBottomPanel extends JPanel {

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