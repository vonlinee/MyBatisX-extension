package com.baomidou.mybatisx.plugin.component;

import java.util.Collection;

public class EnumComboBox<E extends Enum<E>> extends CustomComboBox<ComboBoxItem> {

  public void addItem(E item) {
    addItem(new EnumComboBoxItem(item));
  }

  @SafeVarargs
  public final void addItems(E... items) {
    for (E item : items) {
      addItem(item);
    }
  }

  public void setSelectedItem(E e) {
    super.setSelectedItem(e);
  }

  public void addEnumItems(Collection<E> items) {
    for (E item : items) {
      addItem(item);
    }
  }

  private class EnumComboBoxItem extends SimpleComboBoxItem<E> {

    E e;

    public EnumComboBoxItem(E e) {
      super(e.name(), e, String.class);
      this.e = e;
    }

    @Override
    public String getLabel() {
      return e.name();
    }

    @Override
    public Object getValue() {
      return e;
    }

    @Override
    public Class<?> getValueType() {
      return e.getClass();
    }

    @Override
    public String toString() {
      return e.toString();
    }
  }
}
