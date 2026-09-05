package com.baomidou.mybatisx.plugin.provider;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.FinderRecursivePanel;


public abstract class MyBatisBaseView extends SimpleToolWindowPanel implements Disposable {

  protected final Project myProject;

  protected MyBatisBaseView(Project project) {

    super(false, true);
    myProject = project;
  }

  @Override
  public void dispose() {
  }
}
