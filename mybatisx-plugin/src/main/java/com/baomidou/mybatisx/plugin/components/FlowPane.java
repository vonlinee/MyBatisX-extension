package com.baomidou.mybatisx.plugin.components;

import com.intellij.util.ui.MultiRowFlowPanel;

import java.awt.*;

public class FlowPane extends MultiRowFlowPanel {

  public FlowPane() {
    super(FlowLayout.LEFT, 5, 5);
  }

  /**
   * @param align see {@link java.awt.FlowLayout#LEFT}
   */
  public FlowPane(int align, int hGap, int vGap) {
    super(align, hGap, vGap);
  }

  public void addComponents(Component... components) {
    for (Component component : components) {
      add(component);
    }
  }
}
