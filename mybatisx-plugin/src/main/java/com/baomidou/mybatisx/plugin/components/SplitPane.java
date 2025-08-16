package com.baomidou.mybatisx.plugin.components;

import com.intellij.ui.JBSplitter;

import javax.swing.*;
import java.util.UUID;

/**
 * 分割面板，仅支持两个组件
 */
public class SplitPane extends JBSplitter {

  public SplitPane() {
    this(false, 0.4f);
  }

  public SplitPane(boolean vertical) {
    this(vertical, 0.4f);
  }

  public SplitPane(int proportion) {
    this(false, (float) proportion / 100);
  }

  public SplitPane(float proportion) {
    this(false, proportion);
  }

  public SplitPane(boolean vertical, float proportion) {
    super(vertical, proportion);
    setSplitterProportionKey(UUID.randomUUID().toString());
  }

  public SplitPane(float proportion, JComponent first, JComponent second) {
    super(false, proportion);
    setComponent(first, second);
  }

  public SplitPane(boolean vertical, float proportion, JComponent first, JComponent second) {
    super(vertical, proportion);
    setComponent(first, second);
  }

  public final void setComponent(JComponent first, JComponent second) {
    setFirstComponent(first);
    setSecondComponent(second);
  }

  public void setLeftComponent(JComponent comp) {
    super.setFirstComponent(comp);
  }

  public void setRightComponent(JComponent comp) {
    super.setSecondComponent(comp);
  }
}
