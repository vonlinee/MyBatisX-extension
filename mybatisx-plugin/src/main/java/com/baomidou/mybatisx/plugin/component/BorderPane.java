package com.baomidou.mybatisx.plugin.component;

import com.intellij.util.ui.components.BorderLayoutPanel;

import java.awt.*;

/**
 * BorderLayout
 */
public class BorderPane extends BorderLayoutPanel {

  // same as javafx

  public void setCenter(Component component) {
    addToCenter(component);
  }

  public void setTop(Component component) {
    addToTop(component);
  }

  public void setLeft(Component component) {
    addToLeft(component);
  }

  public void setRight(Component component) {
    addToRight(component);
  }

  public void setBottom(Component component) {
    addToBottom(component);
  }
}
