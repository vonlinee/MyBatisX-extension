package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.feat.mybatis.generator.MyBatisGeneratorPane;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.FinderRecursivePanel;

/**
 * @see com.intellij.spring.toolWindow.SpringBaseView
 * @see FinderRecursivePanel
 */
public class MyBatisGeneratorToolWindowPanel extends SimpleToolWindowPanel implements DumbAware {

  public static final String NAME = "MyBatis Generator";

  protected final Project myProject;

  public MyBatisGeneratorToolWindowPanel(Project myProject) {
    // vertical: 控制工具栏方向。true 表示垂直工具栏，false 表示水平工具栏。
    // borderless：控制面板是否无边框。通常设为 true，可以让面板和 IDE 整体风格更协调。
    super(false, true);
    this.myProject = myProject;
    setContent(new MyBatisGeneratorPane());
  }
}
