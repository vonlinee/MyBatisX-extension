package com.baomidou.mybatisx.plugin.ui.components;

import com.baomidou.mybatisx.plugin.components.Label;
import com.baomidou.mybatisx.plugin.components.Pane;
import com.baomidou.mybatisx.plugin.ui.dialog.SaveOrUpdateDialog;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class DataTypeSaveOrUpdateDialog extends SaveOrUpdateDialog<MultableDataType> {

  String typeGroup;

  TextField identifier;
  TextField minLength;
  TextField maxLength;

  public DataTypeSaveOrUpdateDialog(String typeGroup) {
    this.typeGroup = typeGroup;
  }

  @Override
  protected @Nullable JComponent createCenterPanel() {
    Pane pane = new Pane();

    pane.setLayout(new GridBagLayout());

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = JBUI.insets(5);

    gbc.gridx = 0;
    gbc.gridy = 0;
    pane.add(new JLabel("Group :"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 0;
    TextField typeGroupField = new TextField(15);
    typeGroupField.setText(this.typeGroup);
    typeGroupField.setEnabled(false);
    pane.add(typeGroupField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    pane.add(new Label("Identifier :"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 1;
    pane.add(identifier = new TextField(15), gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    pane.add(new Label("Min Length:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 2;
    pane.add(minLength = new TextField(15), gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    pane.add(new Label("Max Length:"), gbc);

    gbc.gridx = 1;
    gbc.gridy = 3;
    pane.add(maxLength = new TextField(15), gbc);
    return pane;
  }

  @Override
  protected @NotNull MultableDataType createObject() {
    return new DataTypeItem(null);
  }

  @Override
  protected void submit(@NotNull MultableDataType target, boolean saveOrUpdate) {

  }

  @Override
  protected void fill(MultableDataType object, boolean saveOrUpdate) {
    DataTypeItem item = (DataTypeItem) object;
    item.setGroupId(this.typeGroup);
    item.setIdentifier(this.identifier.getText());
    item.setMinLength(Integer.parseInt(this.minLength.getText()));
    item.setMaxLength(Integer.parseInt(this.maxLength.getText()));
  }
}
