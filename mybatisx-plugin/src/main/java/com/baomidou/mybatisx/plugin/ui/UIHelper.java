package com.baomidou.mybatisx.plugin.ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.util.ui.GridBag;

import java.awt.*;

public final class UIHelper {

  private UIHelper() {
  }

  public static GridBag newGridBagLayoutConstraints() {
    return new GridBag()
      .setDefaultAnchor(0, GridBagConstraints.EAST)
      .setDefaultAnchor(1, GridBagConstraints.WEST)
      .setDefaultWeightX(1, 1)
      .setDefaultFill(GridBagConstraints.HORIZONTAL);
  }

  public static GridConstraints newGridLayoutConstraints() {
    return new GridConstraints();
  }
}
