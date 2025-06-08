package com.baomidou.mybatisx.plugin.component;

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
    super(text, horizontalAlignment);
  }

  public Label(String text) {
    super(text);
  }

  public void setPreferredSize(int w, int h) {
    setPreferredSize(new Dimension(w, h));
  }
}
