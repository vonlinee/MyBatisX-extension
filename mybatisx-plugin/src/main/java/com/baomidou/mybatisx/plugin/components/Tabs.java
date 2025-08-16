package com.baomidou.mybatisx.plugin.components;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @see TabPane
 */
public class Tabs extends JBTabsImpl {

  public Tabs(@NotNull Project project) {
    super(project);
  }

  public TabInfo addTab(String title, JComponent component) {
    TabInfo tabInfo = new TabInfo(component);
    tabInfo.setText(title);
    return addTab(tabInfo);
  }

  public void selectTab(int index) {
    select(getTabs().get(index), true);
  }
}
