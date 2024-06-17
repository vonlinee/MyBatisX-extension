package com.baomidou.mybatisx.plugin.ui.dialog;

import com.baomidou.mybatisx.plugin.ui.components.DataTypeTreeView;
import com.baomidou.mybatisx.plugin.ui.components.TypeGroupComboBox;
import com.baomidou.mybatisx.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DataTypeAddDialog extends DialogBase {

    JTextField typeIdTextField;
    DataTypeTreeView treeView;
    TypeGroupComboBox comboBox;

    public DataTypeAddDialog(DataTypeTreeView dataTypeTreeView) {
        super(null);
        setResizable(false);
        setModal(false);
        setSize(400, 150);
        this.treeView = dataTypeTreeView;
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        typeIdTextField = new JTextField();
        comboBox = new TypeGroupComboBox();
        panel.add(new Label("Type Group"));
        panel.add(comboBox);
        panel.add(new Label("Type ID"));
        panel.add(typeIdTextField);
        return panel;
    }

    @Override
    protected @NotNull Action getOKAction() {
        return new DialogWrapperAction("OK") {
            @Override
            protected void doAction(ActionEvent e) {
                String typeGroup = comboBox.getValue();
                String typeId = typeIdTextField.getText();
                if (StringUtils.isEmpty(typeGroup)
                    || StringUtils.isEmpty(typeId)) {
                    return;
                }
                treeView.addDataType(typeGroup, typeId);
                treeView.expandAll();
            }
        };
    }
}
