package com.baomidou.mybatisx.plugin.component;

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

  public void addSpacing(int spacing) {
    this.add(SwingUtils.createHorizontalGlue(spacing));
  }
}
