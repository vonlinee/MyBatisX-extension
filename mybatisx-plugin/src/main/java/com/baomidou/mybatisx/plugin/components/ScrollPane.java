package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.components.JBScrollPane;

import java.awt.*;

public class ScrollPane extends JBScrollPane {

  public ScrollPane() {
  }

  public ScrollPane(Component view) {
    super(view);
  }

  public void setContent(Component component) {
    this.getViewport().setView(component);
  }
}
