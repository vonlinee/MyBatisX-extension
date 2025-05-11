package com.baomidou.mybatisx.plugin.provider;

import com.baomidou.mybatisx.util.IntellijSDK;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

/**
 * @see com.intellij.database.DatabaseToolWindowFactory
 */
public class MyBatisToolWindowFactory implements ToolWindowFactory, DumbAware {

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    toolWindow.setToHideOnEmptyContent(true);

    MyBatisToolWindowView view = IntellijSDK.getService(MyBatisToolWindowView.class, project);

    ContentManager contentManager = toolWindow.getContentManager();
    Content content = contentManager.getFactory().createContent(view.getComponent(), null, false);
    content.setPreferredFocusableComponent(view);
    contentManager.addContent(content);
    contentManager.setSelectedContent(content, true);
  }
}
