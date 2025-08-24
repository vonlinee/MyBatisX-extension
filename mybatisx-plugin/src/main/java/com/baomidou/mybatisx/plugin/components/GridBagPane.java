package com.baomidou.mybatisx.plugin.components;

import javax.swing.*;
import java.awt.*;

public class GridBagPane extends JPanel {

  private final GridBagLayout layoutManager;

  public GridBagPane() {
    super(new GridBagLayout());
    this.layoutManager = (GridBagLayout) getLayout();
  }

  public void addComponent(int column, int row, Component component) {
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = row;
    constraints.gridy = column;
    this.layoutManager.setConstraints(component, constraints);
    this.add(component);
  }
}
