package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.components.JBLabel;

import javax.swing.*;
import java.awt.*;

public class Label extends JBLabel {

  /**
   * @param text                text
   * @param horizontalAlignment horizontalAlignment
   * @see SwingConstants#CENTER
   */
  public Label(String text, int horizontalAlignment) {
    super(text == null ? "" : text, horizontalAlignment);
  }

  public Label(String text) {
    super(text == null ? "" : text);
  }

  public void setPreferredSize(int w, int h) {
    setPreferredSize(new Dimension(w, h));
  }
}
