package com.baomidou.mybatisx.plugin.components;

import com.baomidou.mybatisx.util.SwingUtils;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;

/**
 * @see VerticalLayout
 */
public class VBox extends JPanel {

  public VBox() {
    super(new VerticalLayout());
  }

  public void addSpacing(int spacing) {
    this.add(SwingUtils.createVerticalGlue(spacing));
  }
}
