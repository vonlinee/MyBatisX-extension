package com.baomidou.mybatisx.plugin.component;

import com.baomidou.mybatisx.util.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * @param <E>
 * @see CustomComboBox
 */
public class SimpleComboBox<E> extends com.intellij.openapi.ui.ComboBox<E> {

  public SimpleComboBox() {
    super(new DefaultComboBoxModel<>());
  }

  public SimpleComboBox(@NotNull DefaultComboBoxModel<E> model) {
    super(model);
  }

  public void addItems(Collection<E> items) {
    if (CollectionUtils.isNotEmpty(items)) {
      for (E item : items) {
        addItem(item);
      }
    }
  }

  @Override
  public DefaultComboBoxModel<E> getModel() {
    return (DefaultComboBoxModel<E>) super.getModel();
  }

  public final void clearItems() {
    MutableComboBoxModel<E> model = getModel();
    model.removeElementAt(0);
  }

  public final List<E> getItems() {
    return getModel().getItems();
  }

  public final void setItems(List<E> collection) {
    DefaultComboBoxModel<E> model = getModel();
    model.removeAll();
    model.addAll(0, collection);
  }

  @Override
  @SuppressWarnings("unchecked")
  public E getSelectedItem() {
    return (E) dataModel.getSelectedItem();
  }
}
