package com.baomidou.mybatisx.plugin.intention;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.Project;
import com.intellij.sql.SqlFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

public class ResultSqlEditor extends EditorTextField {
  public ResultSqlEditor(Project project) {
    super(project, SqlFileType.INSTANCE);
    this.setOneLineMode(false);
    this.setEnabled(true);
  }

  @Override
  protected @NotNull EditorEx createEditor() {
    EditorEx editor = super.createEditor();
    HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
    editor.setOneLineMode(false);
    editor.setVerticalScrollbarVisible(true);

    editor.getSettings().setUseTabCharacter(true);
    editor.getSettings().setTabSize(2);
    editor.getSettings().setShowingSpecialChars(true);
    editor.getSettings().setWhitespacesShown(true);
    editor.getSettings().setLineNumbersShown(true);
    editor.getSettings().setUseSoftWraps(true);
    editor.getSettings().setUseCustomSoftWrapIndent(true);
    editor.getSettings().setCustomSoftWrapIndent(2);
    return editor;
  }
}
