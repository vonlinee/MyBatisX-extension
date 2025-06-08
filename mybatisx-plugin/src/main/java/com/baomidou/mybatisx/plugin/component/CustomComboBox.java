package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.SimpleListCellRenderer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;

/**
 * 组合框, 下拉框
 *
 * @see com.intellij.openapi.ui.ComboBox
 * @see SimpleComboBox
 */
public class CustomComboBox<E extends ComboBoxItem> extends com.intellij.openapi.ui.ComboBox<E> {

  public CustomComboBox() {
    super();
    this.setRenderer(new SimpleListCellRenderer<>() {
      @Override
      public void customize(@NotNull JList<? extends E> list, E value, int index, boolean selected, boolean hasFocus) {
        if (value == null) {
          setText("Null");
        } else {
          setText(value.getLabel());
        }
      }
    });
  }

  @SafeVarargs
  public final void addItems(E... items) {
    for (E item : items) {
      addItem(item);
    }
  }

  public final void addItems(Collection<E> items) {
    for (E item : items) {
      addItem(item);
    }
  }

  public final void clearItems() {
    MutableComboBoxModel<E> model = (MutableComboBoxModel<E>) getModel();
    model.removeElementAt(0);
  }
}
