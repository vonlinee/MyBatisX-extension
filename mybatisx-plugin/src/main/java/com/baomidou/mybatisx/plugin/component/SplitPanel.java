package com.baomidou.mybatisx.plugin.component;

import com.intellij.ui.JBSplitter;

import javax.swing.*;

/**
 * 分割面板，仅支持两个组件
 */
public class SplitPanel extends JBSplitter {

    public SplitPanel() {
        this(0.4f);
    }

    public SplitPanel(float proportion) {
        super(false, proportion);
        this.setAutoscrolls(false);
        setDragging(true);
        setDividerPositionStrategy(DividerPositionStrategy.KEEP_FIRST_SIZE);
        setLackOfSpaceStrategy(LackOfSpaceStrategy.HONOR_THE_FIRST_MIN_SIZE);
    }

    public SplitPanel(float proportion, JComponent first, JComponent second) {
        super(false, proportion);
        setComponent(first, second);
    }

    public SplitPanel(boolean vertical, float proportion, JComponent first, JComponent second) {
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
