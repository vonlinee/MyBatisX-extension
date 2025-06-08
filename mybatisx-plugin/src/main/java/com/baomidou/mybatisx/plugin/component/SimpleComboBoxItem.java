package com.baomidou.mybatisx.plugin.component;

import com.baomidou.mybatisx.plugin.component.ComboBoxItem;

public class SimpleComboBoxItem<V> implements ComboBoxItem {

  private final String name;
  private final V value;
  private final Class<?> valueType;

  public SimpleComboBoxItem(String name, V value) {
    this.name = name;
    this.value = value;
    this.valueType = value == null ? Object.class : value.getClass();
  }

  public SimpleComboBoxItem(String name, V value, Class<?> valueType) {
    this.name = name;
    this.value = value;
    this.valueType = valueType;
  }

  @Override
  public String getLabel() {
    return name;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public Class<?> getValueType() {
    return valueType;
  }
}
