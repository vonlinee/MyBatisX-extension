package com.baomidou.mybatisx.plugin.provider;

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
    MyBatisGeneratorToolWindowPanel view = new MyBatisGeneratorToolWindowPanel(project);
    ContentManager contentManager = toolWindow.getContentManager();
    // ToolWindow 的标题显示优先级: 如果 Content 设置了 DisplayName，则显示它, 如果 Content 没有设置，则显示 ToolWindow 的 id
    Content content = contentManager.getFactory().createContent(view, MyBatisGeneratorToolWindowPanel.NAME, false);
    content.setPreferredFocusableComponent(view);
    contentManager.addContent(content);
    contentManager.setSelectedContent(content, true);
  }
}
