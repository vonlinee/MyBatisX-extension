package com.baomidou.mybatisx.feat.jpa.ui;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 选择自动填充时, 再次提示
 */
public class SmartJpaCompletionInsertHandler implements InsertHandler<LookupElement> {
  private final Editor editor;
  private final Project project;

  /**
   * Instantiates a new Smart jpa completion insert handler.
   *
   * @param editor  the editor
   * @param project the project
   */
  public SmartJpaCompletionInsertHandler(Editor editor, Project project) {
    this.editor = editor;
    this.project = project;
  }

  @Override
  public void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
    context.setLaterRunnable(() -> {
      CodeCompletionHandlerBase handler = CodeCompletionHandlerBase.createHandler(CompletionType.BASIC);
      handler.invokeCompletion(project, editor, 1, true);
    });
  }

}
