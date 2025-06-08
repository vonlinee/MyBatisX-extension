package com.baomidou.mybatisx.feat.bean;

import com.baomidou.mybatisx.plugin.component.SimpleComboBoxItem;

public class TranslationAppComboBoxItem extends SimpleComboBoxItem<String> {

  public TranslationAppComboBoxItem(String name, String value) {
    super(name, value, String.class);
  }
}
