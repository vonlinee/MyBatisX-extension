package com.baomidou.mybatisx.plugin.components;

import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;

public class SimpleTextEditor extends EditorTextField {
  public SimpleTextEditor(Project project, FileType fileType) {
    super(project, fileType);
  }

  @Override
  protected @NotNull EditorEx createEditor() {
    EditorEx editor = super.createEditor();
    // 水平滚动条
    HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
    // 禁用单行文本
    editor.setOneLineMode(false);
    // 垂直滚动条
    editor.setVerticalScrollbarVisible(true);
    return editor;
  }
}
