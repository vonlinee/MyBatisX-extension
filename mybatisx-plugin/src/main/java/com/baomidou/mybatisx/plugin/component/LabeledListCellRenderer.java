package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.ui.components.JBLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LabeledListCellRenderer<E> extends SimpleListCellRenderer<E> implements ListCellRenderer<E> {

  @Override
  public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
    JBLabel label = (JBLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    if (value != null) {
      label.setText(getLabelText(value, index, isSelected, cellHasFocus));
    }
    return label;
  }

  public String getLabelText(@NotNull E item, int index, boolean isSelected, boolean cellHasFocus) {
    return Objects.toString(item);
  }

  @Override
  public void customize(@NotNull JList<? extends E> list, E value, int index, boolean selected, boolean hasFocus) {

  }
}
