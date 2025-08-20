package com.baomidou.mybatisx.plugin.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * The type Generic intention.
 *
 * @author yanglin
 */
abstract class GenericIntention(
  /**
   * The Chooser.
   */
  private var chooser: IntentionChooser
) : IntentionAction {

  @Override
  override fun getFamilyName(): String {
    return text
  }

  @Override
  override fun isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean {
    return chooser.isAvailable(project, editor, file)
  }

  @Override
  override fun startInWriteAction(): Boolean {
    return true
  }
}
