package com.baomidou.mybatisx.plugin.component;

public interface ComboBoxItem {

  /**
   * 选项展示的文本
   *
   * @return 选项展示的文本
   */
  String getLabel();

  /**
   * 选项选中的值
   *
   * @return 选项选中的值
   */
  default Object getValue() {
    return this;
  }

  /**
   * 获取值的数据类型
   *
   * @return 值的数据类型
   */
  Class<?> getValueType();
}
