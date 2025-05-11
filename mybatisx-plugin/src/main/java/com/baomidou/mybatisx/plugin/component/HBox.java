package com.baomidou.mybatisx.plugin.component;

import org.jdesktop.swingx.HorizontalLayout;

import java.awt.*;

public class HBox extends Pane {

  public HBox() {
    super(new HorizontalLayout());
  }

  public final void addChildren(Component... components) {
    for (Component component : components) {
      add(component);
    }
  }
}
