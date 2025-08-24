package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.components.JBCheckBox;
import org.jetbrains.annotations.Nullable;

public class CheckBox extends JBCheckBox {

  public CheckBox() {
  }

  public CheckBox(@Nullable String text) {
    super(text);
  }

  public CheckBox(@Nullable String text, boolean selected) {
    super(text, selected);
  }
}
