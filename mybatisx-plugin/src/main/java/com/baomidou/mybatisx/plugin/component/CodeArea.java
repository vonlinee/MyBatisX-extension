package com.baomidou.mybatisx.plugin.component;

import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HorizontalScrollBarEditorCustomization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

/**
 * 支持语法高亮的多行文本
 */
public class CodeArea extends EditorTextField {

  private boolean edited;
  private int lastTextLength;
  private int currentTextLength;
  private boolean initialized;

  public CodeArea() {
    super("", null, PlainTextFileType.INSTANCE);
    addDocumentListener(new DocumentListener() {
      @Override
      public void documentChanged(@NotNull DocumentEvent event) {
        CodeArea.this.edited = true;
        CodeArea.this.currentTextLength = event.getNewLength();
      }
    });
  }

  @Override
  public void setText(@Nullable String text) {
    super.setText(text);
    this.initialized = true;
    this.currentTextLength = this.lastTextLength = getDocument().getTextLength();
  }

  @Override
  protected @NotNull EditorEx createEditor() {
    EditorEx editor = super.createEditor();
    // 水平滚动条
    HorizontalScrollBarEditorCustomization.ENABLED.customize(editor);
    // 禁用单行模式，即多行模式
    editor.setOneLineMode(false);
    // 垂直滚动条
    editor.setVerticalScrollbarVisible(true);
    return editor;
  }

  public boolean isTextLengthChanged() {
    return this.initialized && lastTextLength != currentTextLength;
  }

  public boolean isContentChanged(String text) {
    return Objects.equals(getText(), text);
  }

  public boolean hasChanged(String text) {
    return edited && (isTextLengthChanged() || isContentChanged(text));
  }

  public boolean maybeChanged() {
    return edited;
  }

  /**
   * @param preferredHeight preferred height
   * @see EditorTextField#setPreferredWidth(int)
   */
  public void setPreferredHeight(int preferredHeight) {
    setPreferredSize(new Dimension(getWidth(), preferredHeight));
  }
}
