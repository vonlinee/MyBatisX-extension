package com.baomidou.mybatisx.plugin.components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

public class GridPane extends JPanel {

  private final GridLayoutManager layoutManager;

  public GridPane(int rowCount, int columnCount) {
    super(new GridLayoutManager(rowCount, columnCount));
    this.layoutManager = (GridLayoutManager) getLayout();
  }

  public void addComponent(int row, int column, Component component) {
    this.addComponent(row, column, component, GridConstraints.ANCHOR_EAST);
  }

  /**
   * @param anchor see {@link GridConstraints#ANCHOR_EAST}
   */
  public void addComponent(int row, int column, Component component, int anchor) {
    GridConstraints constraints = new GridConstraints();
    constraints.setRow(row);
    constraints.setColumn(column);
    constraints.setAnchor(anchor);
    this.layoutManager.addLayoutComponent(component, constraints);
    add(component, constraints);
  }

  /**
   * @param anchor see {@link GridConstraints#ANCHOR_EAST}
   */
  public void addComponent(int row, int column, Component component, int anchor, int columnSpan) {
    GridConstraints constraints = new GridConstraints();
    constraints.setRow(row);
    constraints.setColumn(column);
    constraints.setAnchor(anchor);
    constraints.setColSpan(columnSpan);
    this.layoutManager.addLayoutComponent(component, constraints);
    add(component, constraints);
  }
}
