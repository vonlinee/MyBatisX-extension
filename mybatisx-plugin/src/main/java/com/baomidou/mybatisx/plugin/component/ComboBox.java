package com.baomidou.mybatisx.plugin.component;

import com.baomidou.mybatisx.model.ComboBoxItem;

import java.util.Collection;

/**
 * 组合框
 *
 * @see com.intellij.openapi.ui.ComboBox
 * @see SimpleComboBox
 */
public class ComboBox<E extends ComboBoxItem> extends com.intellij.openapi.ui.ComboBox<E> {

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
}
