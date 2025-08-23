package com.baomidou.mybatisx.plugin.components;

import javax.swing.*;
import java.awt.event.MouseListener;

public class Button extends JButton {
  public Button() {
  }

  public Button(String text) {
    super(text);
  }

  public Button(String text, String tooltipText) {
    super(text);
    setToolTipText(tooltipText);
  }

  public Button(String text, String tooltipText, MouseListener mouseListener) {
    super(text);
    setToolTipText(tooltipText);
    addMouseListener(mouseListener);
  }
}
