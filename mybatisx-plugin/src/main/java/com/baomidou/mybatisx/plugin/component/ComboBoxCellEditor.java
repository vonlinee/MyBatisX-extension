package com.baomidou.mybatisx.plugin.component;

import javax.swing.*;
import java.util.List;

public class ComboBoxCellEditor extends com.intellij.util.ui.ComboBoxCellEditor {

  List<String> items;

  public ComboBoxCellEditor(List<String> items) {
    this.items = items;
  }

  @Override
  protected List<String> getComboBoxItems() {
    return items;
  }

  public JComboBox<?> getComboBox() {
    return (JComboBox<?>) this.getComponent();
  }
}
