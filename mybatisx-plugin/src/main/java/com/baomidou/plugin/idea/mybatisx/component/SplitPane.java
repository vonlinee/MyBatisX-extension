package com.baomidou.plugin.idea.mybatisx.component;

import com.intellij.ui.JBSplitter;

import javax.swing.*;

public class SplitPane extends JBSplitter {

    public SplitPane() {
        this.setAutoscrolls(true);
        setDividerPositionStrategy(DividerPositionStrategy.KEEP_FIRST_SIZE);
        setDragging(true);
        setLackOfSpaceStrategy(LackOfSpaceStrategy.HONOR_THE_FIRST_MIN_SIZE);
    }

    public void setLeftComponent(JComponent comp) {
        super.setFirstComponent(comp);
    }

    public void setRightComponent(JComponent comp) {
        super.setSecondComponent(comp);
    }
}
