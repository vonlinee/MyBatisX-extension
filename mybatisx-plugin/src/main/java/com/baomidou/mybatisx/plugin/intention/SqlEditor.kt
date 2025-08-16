package com.baomidou.mybatisx.plugin.intention

import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.project.Project
import com.intellij.sql.SqlFileType
import com.intellij.ui.EditorTextField
import com.intellij.ui.HorizontalScrollBarEditorCustomization

class SqlEditor(project: Project?) : EditorTextField(project, SqlFileType.INSTANCE) {
  init {
    this.isOneLineMode = false
    this.isEnabled = true
  }

  @Override
  override fun createEditor(): EditorEx {
    val editor = super.createEditor()
    HorizontalScrollBarEditorCustomization.ENABLED.customize(editor)
    editor.isOneLineMode = false
    editor.setVerticalScrollbarVisible(true)

    editor.settings.setUseTabCharacter(true)
    editor.settings.setTabSize(2)
    editor.settings.isShowingSpecialChars = true
    editor.settings.isWhitespacesShown = true
    editor.settings.isLineNumbersShown = true
    editor.settings.isUseSoftWraps = true
    editor.settings.isUseCustomSoftWrapIndent = true
    editor.settings.customSoftWrapIndent = 2
    return editor
  }
}
