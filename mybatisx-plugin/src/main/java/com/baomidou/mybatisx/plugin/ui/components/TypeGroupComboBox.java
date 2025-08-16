package com.baomidou.mybatisx.plugin.ui.components;

import com.intellij.openapi.ui.ComboBox;

/**
 * 类型组下拉列表
 */
public class TypeGroupComboBox extends ComboBox<String> {

  public TypeGroupComboBox() {
    super(new String[]{
      "Java",
      "MySQL",
      "Oracle",
      "Kotlin"
    });
  }

  public String getValue() {
    return (String) getSelectedItem();
  }
}
