package com.baomidou.mybatisx.plugin.components;

import javax.swing.*;
import java.awt.*;

public class FlowPane extends JPanel {

  public FlowPane() {
    super(new FlowLayout(FlowLayout.LEFT, 5, 5));
  }

  public void addComponents(Component... components) {
    for (Component component : components) {
      add(component);
    }
  }
}
