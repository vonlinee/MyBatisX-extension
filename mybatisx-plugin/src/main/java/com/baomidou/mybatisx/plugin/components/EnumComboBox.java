package com.baomidou.mybatisx.plugin.components;

import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.Nullable;

public class EnumComboBox<E extends Enum<E>> extends ComboBox<E> {

  public EnumComboBox() {
  }

  public EnumComboBox(Class<E> enumClass) {
    addItems(enumClass.getEnumConstants());
  }

  @Override
  public void addItem(E item) {
    super.addItem(item);
  }

  @SafeVarargs
  public final void addItems(E... items) {
    for (E item : items) {
      addItem(item);
    }
  }

  @Nullable
  @Override
  @SuppressWarnings("unchecked")
  public E getSelectedItem() {
    return (E) super.getSelectedItem();
  }
}
