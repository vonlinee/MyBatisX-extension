package com.baomidou.mybatisx.feat.mybatis;

import com.intellij.icons.AllIcons;
import com.intellij.lang.documentation.ide.actions.ActionsKt;
import com.intellij.lang.documentation.ide.impl.DocumentationBrowser;
import com.intellij.lang.documentation.ide.impl.DocumentationToolWindowManager;
import com.intellij.lang.documentation.ide.ui.DocumentationUI;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.model.Pointer;
import com.intellij.platform.backend.documentation.DocumentationTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Keeps the MyBatis SQL documentation available in the Documentation tool window.
 */
@SuppressWarnings("UnstableApiUsage")
public final class MybatisDocumentationPinAction extends AnAction {

  public MybatisDocumentationPinAction() {
    super("Pin Documentation", "Keep this MyBatis SQL documentation in the tool window", AllIcons.Actions.PinTab);
  }

  @Override
  public void update(@NotNull AnActionEvent event) {
    DocumentationBrowser browser = getBrowser(event);
    boolean available = browser != null && isMybatisDocumentation(browser);
    event.getPresentation().setEnabledAndVisible(available);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    DocumentationBrowser browser = getBrowser(event);
    Project project = event.getProject();
    JBPopup popup = event.getData(ActionsKt.getDOCUMENTATION_POPUP());
    if (browser == null || project == null || popup == null || !isMybatisDocumentation(browser)) {
      return;
    }

    DocumentationUI ui = browser.getUi();
    popup.cancel();
    DocumentationToolWindowManager.Companion.getInstance(project).showInToolWindow(ui);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.EDT;
  }

  @Nullable
  private static DocumentationBrowser getBrowser(@NotNull AnActionEvent event) {
    return ActionsKt.documentationBrowser(event.getDataContext());
  }

  private static boolean isMybatisDocumentation(@NotNull DocumentationBrowser browser) {
    Pointer<? extends DocumentationTarget> pointer = browser.getTargetPointer();
    DocumentationTarget target = pointer.dereference();
    return target instanceof MybatisSqlDocumentationTarget;
  }
}
