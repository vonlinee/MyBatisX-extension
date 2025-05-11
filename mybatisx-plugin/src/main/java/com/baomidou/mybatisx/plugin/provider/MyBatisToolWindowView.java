package com.baomidou.mybatisx.plugin.provider;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;

@State(
  name = "MyBatisToolWindowView",
  storages = {@Storage("$PRODUCT_WORKSPACE_FILE$")}
)
public class MyBatisToolWindowView extends MyBatisBaseView implements DumbAware {

  public static final String NAME = "MyBatis";

  public MyBatisToolWindowView(Project myProject) {
    super(myProject);
  }

  @Override
  public void dispose() {

  }

}
