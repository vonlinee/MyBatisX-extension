package com.baomidou.mybatisx.plugin.ui.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

/**
 * base class for dialog in application.
 */
public abstract class DialogBase extends DialogWrapper {

  private Project project;

  public DialogBase() {
    super(null);
  }

  protected DialogBase(@Nullable Project project) {
    super(project);
    this.project = project;
  }

  @Override
  public void show() {
    if (!isDisposed()) {
      super.init();
    }
    super.show();
  }

  @Override
  protected final void dispose() {
    if (isDisposed()) {
      return;
    }
    beforeDisposed();
    super.dispose(); // this will free the resources associated with the dialog.
  }

  protected void beforeDisposed() {
  }


  public final void closeByCancel() {
    close(DialogWrapper.CANCEL_EXIT_CODE);
  }

  public final void closeByOk() {
    close(DialogWrapper.OK_EXIT_CODE);
  }

  public final Project getProject() {
    return project;
  }
}
