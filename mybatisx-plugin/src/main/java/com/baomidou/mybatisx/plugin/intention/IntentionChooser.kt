package com.baomidou.mybatisx.plugin.intention

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

/**
 * The interface Intention chooser.
 *
 * @author yanglin
 */
interface IntentionChooser {
  /**
   * Is available boolean.
   *
   * @param project the project
   * @param editor  the editor
   * @param file    the file
   * @return the boolean
   */
  fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean
}
