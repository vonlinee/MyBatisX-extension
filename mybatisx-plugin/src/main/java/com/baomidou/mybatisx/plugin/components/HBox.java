package com.baomidou.mybatisx.plugin.components;

import com.baomidou.mybatisx.util.SwingUtils;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import java.awt.*;

/**
 * @see org.jdesktop.swingx.HorizontalLayout this is included in intellij sdk
 * @see Box
 * @see com.intellij.ui.components.JBBox
 */
public class HBox extends Pane {

  public HBox() {
    super(new HorizontalLayout());
  }

  public final void addChildren(Component... components) {
    for (Component component : components) {
      add(component);
    }
  }

  public final void addChildrenWithSpacing(int spacing, Component... components) {
    if (components.length == 1) {
      add(components[0]);
    } else {
      for (int i = 0; i < components.length - 2; i++) {
        add(components[i]);
        addSpacing(spacing);
      }
      add(components[components.length - 1]);
    }
  }

  public void addSpacing(int spacing) {
    this.add(SwingUtils.createHorizontalGlue(spacing));
  }
}
